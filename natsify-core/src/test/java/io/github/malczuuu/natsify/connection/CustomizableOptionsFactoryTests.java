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

package io.github.malczuuu.natsify.connection;

import static org.assertj.core.api.Assertions.assertThat;

import io.nats.client.Options;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomizableOptionsFactoryTests {

  private CustomizableOptionsFactory connectionOptionsFactory;

  @BeforeEach
  void beforeEach() {
    connectionOptionsFactory = new CustomizableOptionsFactory();
  }

  @Test
  void givenNoCustomizers_whenGetOptions_thenReturnsDefaultOptions() {
    Options options = connectionOptionsFactory.getOptions();

    assertThat(options).isNotNull();
  }

  @Test
  void givenSingleCustomizer_whenGetOptions_thenCustomizerApplied() {
    connectionOptionsFactory.register(builder -> builder.connectionName("test-connection"));

    Options options = connectionOptionsFactory.getOptions();

    assertThat(options.getConnectionName()).isEqualTo("test-connection");
  }

  @Test
  void givenMultipleCustomizers_whenGetOptions_thenAllCustomizersApplied() {
    connectionOptionsFactory.register(builder -> builder.connectionName("test-connection"));
    connectionOptionsFactory.register(builder -> builder.noRandomize().bufferSize(2048));

    Options options = connectionOptionsFactory.getOptions();

    assertThat(options.getConnectionName()).isEqualTo("test-connection");
    assertThat(options.isNoRandomize()).isTrue();
    assertThat(options.getBufferSize()).isEqualTo(2048);
  }

  @Test
  void givenMultipleCustomizers_whenGetOptions_thenAppliedInOrder() {
    connectionOptionsFactory.register(builder -> builder.connectionName("first"));
    connectionOptionsFactory.register(builder -> builder.connectionName("second"));

    Options options = connectionOptionsFactory.getOptions();

    assertThat(options.getConnectionName()).isEqualTo("second");
  }

  @Test
  void givenCustomizerReturningNewBuilder_whenGetOptions_thenNewBuilderIsUsed() {
    connectionOptionsFactory.register(
        ignored -> new Options.Builder().connectionName("from-new-builder"));

    Options options = connectionOptionsFactory.getOptions();

    assertThat(options.getConnectionName()).isEqualTo("from-new-builder");
  }
}
