/*
 * Copyright 2026-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.malczuuu.natsify.handler;

import io.github.malczuuu.natsify.annotation.AckMode;
import io.github.malczuuu.natsify.instrument.JetStreamListenerObserver;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.impl.Headers;
import io.nats.client.impl.NatsJetStreamMetaData;
import io.nats.client.impl.NatsMessage;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

final class JetStreamInvocation implements Consumer<Message> {

  private static final Logger log = LoggerFactory.getLogger(JetStreamInvocation.class);

  private final JetStreamListenerDetails listener;
  private final MessageArgumentResolver argumentResolver;
  private final JetStreamListenerObserver observer;
  private final Connection connection;

  JetStreamInvocation(
      JetStreamListenerDetails listener,
      MessageArgumentResolver argumentResolver,
      JetStreamListenerObserver observer,
      Connection connection) {
    this.listener = listener;
    this.argumentResolver = argumentResolver;
    this.observer = observer;
    this.connection = connection;
  }

  @Override
  public void accept(Message msg) {
    observer.onReceived(listener.getSubject(), listener.getStream());
    long start = System.nanoTime();
    try {
      doAccept(msg);
    } finally {
      observer.onProcessed(listener.getSubject(), listener.getStream(), System.nanoTime() - start);
    }
  }

  private void doAccept(Message msg) {
    Object[] args;
    try {
      args = argumentResolver.resolveArguments(listener.getMethod().getParameters(), msg);
    } catch (Exception e) {
      logResolutionException(msg, e);
      msg.term();
      observer.onTerminated(listener.getSubject(), listener.getStream(), e);
      if (!listener.getDeadLetterSubject().isEmpty()) {
        publishDeadLetter(msg, e);
        observer.onDeadLettered(listener.getSubject(), listener.getStream());
      }
      return;
    }

    try {
      listener.getMethod().invoke(listener.getBean(), args);
      if (listener.getAckMode() == AckMode.AUTO) {
        msg.ack();
        observer.onAcked(listener.getSubject(), listener.getStream());
      }
    } catch (InvocationTargetException | IllegalAccessException e) {
      log.error("Failed to invoke handler for NATS JetStream listener {}", listener.getMethod(), e);
      if (listener.getAckMode() == AckMode.AUTO) {
        if (isLastDelivery(msg)) {
          msg.term();
          publishDeadLetter(msg, e);
          observer.onDeadLettered(listener.getSubject(), listener.getStream());
        } else {
          msg.nak();
          observer.onNacked(listener.getSubject(), listener.getStream());
        }
      }
    }
  }

  private boolean isLastDelivery(Message msg) {
    if (listener.getDeadLetterSubject().isEmpty()) {
      return false;
    }
    NatsJetStreamMetaData meta = msg.metaData();
    return meta != null && meta.deliveredCount() >= listener.getMaxDeliveries();
  }

  private void publishDeadLetter(Message msg, @Nullable Exception cause) {
    try {
      Headers dlqHeaders = buildDeadLetterHeaders(msg, cause);
      byte[] body = msg.getData() != null ? msg.getData() : new byte[0];
      Message dlqMsg =
          NatsMessage.builder()
              .subject(listener.getDeadLetterSubject())
              .headers(dlqHeaders)
              .data(body)
              .build();
      connection.publish(dlqMsg);
    } catch (Exception e) {
      log.error(
          "Failed to publish dead-letter message to subject {}",
          listener.getDeadLetterSubject(),
          e);
    }
  }

  private Headers buildDeadLetterHeaders(Message msg, @Nullable Exception cause) {
    Headers headers = new Headers();
    Headers origHeaders = msg.getHeaders();
    if (origHeaders != null) {
      for (String key : origHeaders.keySet()) {
        List<String> values = origHeaders.get(key);
        if (values != null && !values.isEmpty()) {
          headers.add(key, values);
        }
      }
    }
    headers.add("X-Dead-Letter-Subject", listener.getSubject());
    headers.add("X-Dead-Letter-Stream", listener.getStream());
    headers.add("X-Dead-Letter-Consumer", listener.getDurable());
    NatsJetStreamMetaData meta = msg.metaData();
    if (meta != null) {
      headers.add("X-Dead-Letter-Delivery", String.valueOf(meta.deliveredCount()));
    }
    if (cause != null) {
      Throwable root = cause instanceof InvocationTargetException ite ? ite.getCause() : cause;
      String message = root != null ? root.getMessage() : null;
      String reason =
          (root != null ? root.getClass().getSimpleName() : cause.getClass().getSimpleName())
              + (message != null ? ": " + truncate(message, 200) : "");
      headers.add("X-Dead-Letter-Reason", reason);
    }
    headers.add("X-Dead-Letter-Timestamp", Instant.now().toString());
    return headers;
  }

  private String truncate(String reason, int maxLength) {
    return reason.length() <= maxLength ? reason : reason.substring(0, maxLength) + "...";
  }

  private void logResolutionException(Message msg, Exception e) {
    String stream = null;
    Long streamSequence = null;
    Long consumerSequence = null;
    Long deliveredCount = null;
    Instant timestamp = null;

    if (msg.metaData() != null) {
      stream = msg.metaData().getStream();
      streamSequence = msg.metaData().streamSequence();
      consumerSequence = msg.metaData().consumerSequence();
      deliveredCount = msg.metaData().deliveredCount();
      timestamp = msg.metaData().timestamp().toInstant();
    }

    log.error(
        "Unable to resolve arguments for NATS JetStream handler {}.{}, terminating message, subject={}, stream={}, streamSequence={}, consumerSequence={}, deliveredCount={}, timestamp={}",
        AopUtils.getTargetClass(listener.getBean()).getSimpleName(),
        listener.getMethod().getName(),
        msg.getSubject(),
        stream,
        streamSequence,
        consumerSequence,
        deliveredCount,
        timestamp,
        e);
  }
}
