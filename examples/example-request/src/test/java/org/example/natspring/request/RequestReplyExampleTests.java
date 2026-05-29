package org.example.natspring.request;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.amadeusitgroup.testcontainers.nats.NatsContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class RequestReplyExampleTests {

  @Container @ServiceConnection
  public static final NatsContainer nats = new NatsContainer("nats:2.14.0");

  @Autowired RestTestClient restClient;

  @Test
  void addEndpointSendsRequestAndReturnsSum() {
    restClient
        .get()
        .uri("/add?a=3&b=4")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(MathResult.class)
        .value(result -> assertThat(result.sum()).isEqualTo(7));
  }

  @Test
  void echoEndpointSendsRequestAndReturnsText() {
    restClient
        .get()
        .uri("/echo?text=hello")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(String.class)
        .value(result -> assertThat(result).isEqualTo("hello"));
  }
}
