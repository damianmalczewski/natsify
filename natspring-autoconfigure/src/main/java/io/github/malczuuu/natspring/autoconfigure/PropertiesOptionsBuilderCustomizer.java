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

package io.github.malczuuu.natspring.autoconfigure;

import io.github.malczuuu.natspring.connection.ConnectionOptionsBuilderCustomizer;
import io.nats.client.Options;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

final class PropertiesOptionsBuilderCustomizer implements ConnectionOptionsBuilderCustomizer {

  private static final String APP_NAME_PROPERTY = "spring.application.name";

  private final Environment environment;
  private final NatsProperties properties;
  private final NatsConnectionDetails connectionDetails;

  PropertiesOptionsBuilderCustomizer(
      Environment environment, NatsProperties properties, NatsConnectionDetails connectionDetails) {
    this.environment = environment;
    this.properties = properties;
    this.connectionDetails = connectionDetails;
  }

  @Override
  public Options.Builder customize(Options.Builder builder) {
    String connectionName = properties.getConnectionName();
    if (connectionName == null) {
      connectionName = environment.getProperty(APP_NAME_PROPERTY);
    }

    if (StringUtils.hasLength(connectionDetails.getUsername())) {
      builder = builder.userInfo(connectionDetails.getUsername(), connectionDetails.getPassword());
    }

    if (properties.isNoEcho()) {
      builder = builder.noEcho();
    }
    if (properties.isNoRandomize()) {
      builder = builder.noRandomize();
    }
    if (properties.getInboxPrefix() != null) {
      builder = builder.inboxPrefix(properties.getInboxPrefix());
    }
    if (properties.getConnectionTimeout() != null) {
      builder = builder.connectionTimeout(properties.getConnectionTimeout());
    }
    if (properties.getSocketWriteTimeout() != null) {
      builder = builder.socketWriteTimeout(properties.getSocketWriteTimeout());
    }
    if (properties.getMaxReconnects() != null) {
      builder = builder.maxReconnects(properties.getMaxReconnects());
    }
    if (properties.getReconnectWait() != null) {
      builder = builder.reconnectWait(properties.getReconnectWait());
    }
    if (properties.getReconnectJitter() != null) {
      builder = builder.reconnectJitter(properties.getReconnectJitter());
    }
    if (properties.getReconnectJitterTls() != null) {
      builder = builder.reconnectJitterTls(properties.getReconnectJitterTls());
    }
    if (properties.getReconnectBufferSize() != null) {
      builder = builder.reconnectBufferSize(properties.getReconnectBufferSize());
    }
    if (properties.getPingInterval() != null) {
      builder = builder.pingInterval(properties.getPingInterval());
    }
    if (properties.getMaxPingsOut() != null) {
      builder = builder.maxPingsOut(properties.getMaxPingsOut());
    }
    if (properties.getRequestCleanupInterval() != null) {
      builder = builder.requestCleanupInterval(properties.getRequestCleanupInterval());
    }

    return builder.server(connectionDetails.getServer()).connectionName(connectionName);
  }
}
