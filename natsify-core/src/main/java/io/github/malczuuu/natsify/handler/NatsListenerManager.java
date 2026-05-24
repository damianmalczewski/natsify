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

import io.github.malczuuu.natsify.instrument.NatsListenerObserver;
import io.nats.client.Connection;
import java.util.ArrayList;
import java.util.List;

/** Manages NATS Core subscription handlers for registered {@link NatsListenerDetails listeners}. */
public class NatsListenerManager implements ListenerManager {

  private final NatsListenerRegistry natsListenerRegistry;
  private final MessageArgumentResolver argumentResolver;
  private final NatsListenerObserver observer;

  private final List<NatsListenerHandler> handlers = new ArrayList<>();

  /**
   * Creates a new {@code NatsListenerManager}.
   *
   * @param natsListenerRegistry registry of listener details to initialize
   * @param argumentResolver resolver used to map message data to handler method arguments
   * @param observer observer notified on listener invocations
   */
  public NatsListenerManager(
      NatsListenerRegistry natsListenerRegistry,
      MessageArgumentResolver argumentResolver,
      NatsListenerObserver observer) {
    this.natsListenerRegistry = natsListenerRegistry;
    this.argumentResolver = argumentResolver;
    this.observer = observer;
  }

  /**
   * Initializes and starts all handlers using the given NATS connection. Creates a subscription
   * handler for each registered {@link NatsListenerDetails}.
   *
   * @param connection the active NATS connection
   * @throws Exception if any handler fails to start
   */
  @Override
  public synchronized void initialize(Connection connection) throws Exception {
    for (NatsListenerDetails listener : natsListenerRegistry.getListeners()) {
      NatsListenerHandler handler =
          new SubscriptionHandler(
              connection,
              listener,
              new NatsListenerInvocation(listener, argumentResolver, observer));
      handlers.add(handler);
      handler.start();
    }
  }

  /** Stops all active handlers. */
  @Override
  public synchronized void stop() {
    handlers.forEach(NatsListenerHandler::stop);
    handlers.clear();
  }
}
