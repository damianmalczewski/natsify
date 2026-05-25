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

package io.github.malczuuu.natsify.autoconfigure;

import io.nats.client.Options;
import java.time.Duration;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/** Configuration properties for Natsify Project, bound under the {@code natsify} prefix. */
@ConfigurationProperties(prefix = "natsify")
public class NatsProperties {

  /** Whether NATS auto-configuration is enabled. Default: {@code true}. */
  private final boolean enabled;

  /** NATS server URL. Default: {@code nats://localhost:4222}. */
  private final String server;

  /** Username for NATS authentication. Omit if the server requires no credentials. */
  private final @Nullable String username;

  /** Password for NATS authentication. Omit if the server requires no credentials. */
  private final @Nullable String password;

  /** Optional name for the NATS connection. Used as the thread name in the client. */
  private final @Nullable String connectionName;

  /**
   * Maximum time to wait when establishing a connection. Uses the client default if {@code null}.
   */
  private final Duration connectionTimeout;

  /**
   * Maximum time to wait for a socket write to complete. Uses the client default if {@code null}.
   */
  private final Duration socketWriteTimeout;

  /**
   * Whether declared {@code StreamConfiguration} beans are used to create or update JetStream
   * streams on startup. Default: {@code false}.
   */
  private final boolean autoStreamCreation;

  /**
   * Number of messages fetched per poll cycle for JetStream pull consumers. Default: {@code 200}.
   */
  private final int pullFetchBatchSize;

  /**
   * Maximum time to wait for messages in each fetch call for JetStream pull consumers. Default:
   * {@code 200ms}.
   */
  private final Duration pullFetchTimeout;

  /**
   * Creates a new {@code NatsProperties} instance. Intended for use by the Spring Boot
   * configuration binding mechanism; prefer injecting the bound bean over constructing directly.
   *
   * @param enabled whether auto-configuration is enabled
   * @param server the NATS server URL
   * @param username optional username for authentication
   * @param password optional password for authentication
   * @param connectionName optional name for the connection, used as thread name
   * @param connectionTimeout optional maximum time to wait when establishing a connection
   * @param socketWriteTimeout optional maximum time to wait for a socket write to complete
   * @param autoStreamCreation whether JetStream streams should be created or updated on startup
   * @param pullFetchBatchSize number of messages to fetch per poll cycle for pull consumers
   * @param pullFetchTimeout maximum time to wait for messages in each fetch call for pull consumers
   */
  public NatsProperties(
      @DefaultValue("true") boolean enabled,
      @DefaultValue("nats://localhost:4222") String server,
      @Nullable String username,
      @Nullable String password,
      @Nullable String connectionName,
      @Nullable Duration connectionTimeout,
      @Nullable Duration socketWriteTimeout,
      @DefaultValue("false") boolean autoStreamCreation,
      @DefaultValue("200") int pullFetchBatchSize,
      @DefaultValue("200ms") Duration pullFetchTimeout) {
    this.enabled = enabled;
    this.server = server;
    this.username = username;
    this.password = password;
    this.connectionName = connectionName;
    this.connectionTimeout =
        connectionTimeout != null ? connectionTimeout : Options.DEFAULT_CONNECTION_TIMEOUT;
    this.socketWriteTimeout =
        socketWriteTimeout != null ? socketWriteTimeout : Options.DEFAULT_SOCKET_WRITE_TIMEOUT;
    this.autoStreamCreation = autoStreamCreation;
    this.pullFetchBatchSize = pullFetchBatchSize;
    this.pullFetchTimeout = pullFetchTimeout;
  }

  /**
   * Returns whether NATS auto-configuration is enabled.
   *
   * @return whether NATS auto-configuration is enabled
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Returns the NATS server URL.
   *
   * @return the NATS server URL
   */
  public String getServer() {
    return server;
  }

  /**
   * Returns the username for authentication, or {@code null}.
   *
   * @return the username for authentication, or {@code null}
   */
  public @Nullable String getUsername() {
    return username;
  }

  /**
   * Returns the password for authentication, or {@code null}.
   *
   * @return the password for authentication, or {@code null}
   */
  public @Nullable String getPassword() {
    return password;
  }

  /**
   * Returns the name of the connection, or {@code null} if not specified. Used in thread name.
   *
   * @return the name of the connection, or {@code null}
   */
  public @Nullable String getConnectionName() {
    return connectionName;
  }

  /**
   * Returns the maximum time to wait when establishing a connection, or {@code null} to use the
   * client default.
   *
   * @return the connection timeout, or {@code null}
   */
  public Duration getConnectionTimeout() {
    return connectionTimeout;
  }

  /**
   * Returns the maximum time to wait for a socket write to complete, or {@code null} to use the
   * client default.
   *
   * @return the socket write timeout, or {@code null}
   */
  public Duration getSocketWriteTimeout() {
    return socketWriteTimeout;
  }

  /**
   * Returns whether JetStream stream auto-creation is enabled.
   *
   * @return whether JetStream stream auto-creation is enabled
   */
  public boolean isAutoStreamCreation() {
    return autoStreamCreation;
  }

  /**
   * Returns the number of messages to fetch per poll cycle for JetStream pull consumers.
   *
   * @return the pull fetch batch size
   */
  public int getPullFetchBatchSize() {
    return pullFetchBatchSize;
  }

  /**
   * Returns the maximum time to wait for messages in each fetch call for JetStream pull consumers.
   *
   * @return the pull fetch timeout
   */
  public Duration getPullFetchTimeout() {
    return pullFetchTimeout;
  }
}
