package org.example.natspring.telemetry;

import io.nats.client.api.StreamConfiguration;
import java.time.Clock;
import java.time.ZoneId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TelemetryApplication {

  @Bean
  Clock clock() {
    return Clock.system(ZoneId.systemDefault());
  }

  @Bean
  StreamConfiguration iotRawStream() {
    return StreamConfiguration.builder().name("IOT_RAW").subjects("iot.events.raw").build();
  }

  @Bean
  StreamConfiguration iotProcessedStream() {
    return StreamConfiguration.builder()
        .name("IOT_PROCESSED")
        .subjects("iot.events.processed")
        .build();
  }

  @Bean
  StreamConfiguration iotDlqStream() {
    return StreamConfiguration.builder().name("IOT_DLQ").subjects("iot.events.deadletter").build();
  }

  public static void main(String[] args) {
    SpringApplication.run(TelemetryApplication.class, args);
  }
}
