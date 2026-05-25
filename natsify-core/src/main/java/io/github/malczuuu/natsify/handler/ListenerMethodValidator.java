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

import io.github.malczuuu.natsify.annotation.NatsHeader;
import io.github.malczuuu.natsify.annotation.NatsHeaders;
import io.github.malczuuu.natsify.annotation.NatsSubject;
import io.nats.client.impl.Headers;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

class ListenerMethodValidator {

  static void validate(Method method) {
    Parameter[] params = method.getParameters();
    for (int i = 0; i < params.length; i++) {
      validateParameter(method, params[i], i);
    }
  }

  private static void validateParameter(Method method, Parameter param, int index) {
    NatsHeader natsHeader = param.getAnnotation(NatsHeader.class);
    if (natsHeader != null) {
      String name = natsHeader.value().isEmpty() ? natsHeader.name() : natsHeader.value();
      if (name.isEmpty()) {
        throw new IllegalArgumentException(
            "Parameter "
                + index
                + " of "
                + method.toGenericString()
                + ": @NatsHeader requires a non-empty name");
      }
      Class<?> type = param.getType();
      if (type != String.class && type != String[].class && !List.class.isAssignableFrom(type)) {
        throw new IllegalArgumentException(
            "Parameter "
                + index
                + " of "
                + method.toGenericString()
                + ": @NatsHeader parameter must be String, String[], or List<String>");
      }
    }
    if (param.isAnnotationPresent(NatsSubject.class)) {
      if (param.getType() != String.class) {
        throw new IllegalArgumentException(
            "Parameter "
                + index
                + " of "
                + method.toGenericString()
                + ": @NatsSubject parameter must be String");
      }
    }
    if (param.isAnnotationPresent(NatsHeaders.class)) {
      if (!Headers.class.isAssignableFrom(param.getType())) {
        throw new IllegalArgumentException(
            "Parameter "
                + index
                + " of "
                + method.toGenericString()
                + ": @NatsHeaders parameter must be assignable from Headers");
      }
    }
  }
}
