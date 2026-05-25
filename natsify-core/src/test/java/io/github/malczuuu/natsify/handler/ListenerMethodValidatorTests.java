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

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.malczuuu.natsify.annotation.NatsHeader;
import io.github.malczuuu.natsify.annotation.NatsHeaders;
import io.github.malczuuu.natsify.annotation.NatsSubject;
import io.nats.client.impl.Headers;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.Test;

class ListenerMethodValidatorTests {

  @SuppressWarnings("unused")
  static void validNoParams() {}

  @SuppressWarnings("unused")
  static void validNatsHeaderString(@NatsHeader("X-Type") String h) {}

  @SuppressWarnings("unused")
  static void validNatsHeaderList(@NatsHeader("X-Type") List<String> h) {}

  @SuppressWarnings("unused")
  static void validNatsHeaderArray(@NatsHeader("X-Type") String[] h) {}

  @SuppressWarnings("unused")
  static void validNatsSubjectString(@NatsSubject String subject) {}

  @SuppressWarnings("unused")
  static void validNatsHeadersType(@NatsHeaders Headers headers) {}

  @SuppressWarnings("unused")
  static void invalidNatsHeaderEmptyName(@NatsHeader String h) {}

  @SuppressWarnings("unused")
  static void invalidNatsHeaderWrongType(@NatsHeader("X-Type") int h) {}

  @SuppressWarnings("unused")
  static void invalidNatsSubjectWrongType(@NatsSubject int subject) {}

  @SuppressWarnings("unused")
  static void invalidNatsHeadersWrongType(@NatsHeaders String headers) {}

  private static Method method(String name, Class<?>... paramTypes) {
    try {
      return ListenerMethodValidatorTests.class.getDeclaredMethod(name, paramTypes);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void givenNoParams_whenValidate_thenNoException() {
    assertThatCode(() -> ListenerMethodValidator.validate(method("validNoParams")))
        .doesNotThrowAnyException();
  }

  @Test
  void givenNatsHeaderStringParam_whenValidate_thenNoException() {
    assertThatCode(
            () -> ListenerMethodValidator.validate(method("validNatsHeaderString", String.class)))
        .doesNotThrowAnyException();
  }

  @Test
  void givenNatsHeaderListParam_whenValidate_thenNoException() {
    assertThatCode(
            () -> ListenerMethodValidator.validate(method("validNatsHeaderList", List.class)))
        .doesNotThrowAnyException();
  }

  @Test
  void givenNatsHeaderArrayParam_whenValidate_thenNoException() {
    assertThatCode(
            () -> ListenerMethodValidator.validate(method("validNatsHeaderArray", String[].class)))
        .doesNotThrowAnyException();
  }

  @Test
  void givenNatsSubjectStringParam_whenValidate_thenNoException() {
    assertThatCode(
            () -> ListenerMethodValidator.validate(method("validNatsSubjectString", String.class)))
        .doesNotThrowAnyException();
  }

  @Test
  void givenNatsHeadersTypeParam_whenValidate_thenNoException() {
    assertThatCode(
            () -> ListenerMethodValidator.validate(method("validNatsHeadersType", Headers.class)))
        .doesNotThrowAnyException();
  }

  @Test
  void givenNatsHeaderWithEmptyName_whenValidate_thenThrowsIllegalArgumentException() {
    assertThatThrownBy(
            () ->
                ListenerMethodValidator.validate(
                    method("invalidNatsHeaderEmptyName", String.class)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("@NatsHeader requires a non-empty name");
  }

  @Test
  void givenNatsHeaderWithWrongType_whenValidate_thenThrowsIllegalArgumentException() {
    assertThatThrownBy(
            () -> ListenerMethodValidator.validate(method("invalidNatsHeaderWrongType", int.class)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("@NatsHeader parameter must be String, String[], or List<String>");
  }

  @Test
  void givenNatsSubjectWithWrongType_whenValidate_thenThrowsIllegalArgumentException() {
    assertThatThrownBy(
            () ->
                ListenerMethodValidator.validate(method("invalidNatsSubjectWrongType", int.class)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("@NatsSubject parameter must be String");
  }

  @Test
  void givenNatsHeadersWithWrongType_whenValidate_thenThrowsIllegalArgumentException() {
    assertThatThrownBy(
            () ->
                ListenerMethodValidator.validate(
                    method("invalidNatsHeadersWrongType", String.class)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("@NatsHeaders parameter must be assignable from Headers");
  }
}
