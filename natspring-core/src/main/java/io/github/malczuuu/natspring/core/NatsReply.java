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

package io.github.malczuuu.natspring.core;

import io.nats.client.Message;
import org.springframework.core.ParameterizedTypeReference;

/**
 * Represents a reply message received via NATS request/reply.
 *
 * @since 0.1.0
 */
public interface NatsReply {

  /**
   * Returns the raw NATS reply message.
   *
   * @return the underlying {@link Message}
   */
  Message getMessage();

  /**
   * Deserializes the reply body as an instance of the given type.
   *
   * @param type the target type
   * @param <T> the target type
   * @return deserialized value
   */
  <T> T bodyAs(Class<T> type);

  /**
   * Deserializes the reply body as an instance of the given generic type.
   *
   * @param typeReference the target type reference
   * @param <T> the target type
   * @return deserialized value
   */
  <T> T bodyAs(ParameterizedTypeReference<T> typeReference);
}
