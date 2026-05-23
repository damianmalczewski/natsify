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

package org.example.natsify.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import io.github.amadeusitgroup.testcontainers.nats.NatsContainer;
import io.github.malczuuu.natsify.core.NatsOperations;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import org.example.natsify.common.Record;
import org.example.natsify.common.RecordStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class NatsListenerExampleTests {

  @Container @ServiceConnection
  static final NatsContainer natsContainer = new NatsContainer("nats:2.14.0").withJetStream();

  @Autowired NatsOperations natsOperations;
  @Autowired RestTestClient restClient;
  @Autowired RecordStore recordStore;

  @BeforeEach
  void beforeEach() {
    recordStore.clear();
  }

  @Test
  void listenerReceivesMeasurementAndEndpointExposesIt() {
    natsOperations.publish(
        "telemetry.temperature",
        new Record("sensor-single", 1700000000.0, "temperature", 23.5, "Cel"));

    await().atMost(Duration.ofSeconds(5)).until(() -> !measurementsFor("sensor-single").isEmpty());

    restClient
        .get()
        .uri("/telemetry")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Record[].class)
        .value(
            all -> {
              List<Record> received = measurementsFor("sensor-single", all);
              assertThat(received).hasSize(1);
              assertThat(received.getFirst().n()).isEqualTo("temperature");
              assertThat(received.getFirst().v()).isEqualTo(23.5);
              assertThat(received.getFirst().u()).isEqualTo("Cel");
            });
  }

  @Test
  void multipleSubjectsWithinStreamAreAllReceived() {
    natsOperations.publish(
        "telemetry.humidity", new Record("sensor-multi", 1700000001.0, "humidity", 65.0, "%RH"));
    natsOperations.publish(
        "telemetry.pressure", new Record("sensor-multi", 1700000002.0, "pressure", 1013.25, "hPa"));

    await().atMost(Duration.ofSeconds(5)).until(() -> measurementsFor("sensor-multi").size() >= 2);

    restClient
        .get()
        .uri("/telemetry")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Record[].class)
        .value(
            all -> {
              List<Record> received = measurementsFor("sensor-multi", all);
              assertThat(received).hasSize(2);
              assertThat(received.get(0).n()).isEqualTo("humidity");
              assertThat(received.get(1).n()).isEqualTo("pressure");
            });
  }

  private List<Record> measurementsFor(String bn) {
    Record[] all =
        restClient
            .get()
            .uri("/telemetry")
            .exchange()
            .expectBody(Record[].class)
            .returnResult()
            .getResponseBody();
    return measurementsFor(bn, all);
  }

  private static List<Record> measurementsFor(String bn, Record[] all) {
    if (all == null) return List.of();
    return Arrays.stream(all).filter(m -> bn.equals(m.bn())).toList();
  }
}
