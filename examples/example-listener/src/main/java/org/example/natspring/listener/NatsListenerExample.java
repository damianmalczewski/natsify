package org.example.natspring.listener;

import io.github.malczuuu.natspring.annotation.NatsListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class NatsListenerExample {

  private static final Logger log = LoggerFactory.getLogger(NatsListenerExample.class);

  private final List<SenmlRecord> records = new CopyOnWriteArrayList<>();

  @GetMapping(path = "/telemetry")
  public List<SenmlRecord> getAll() {
    return List.copyOf(records);
  }

  @NatsListener(subject = "telemetry.>")
  public void onRecord(SenmlRecord record) {
    records.add(record);
    log.info("Received telemetry on NatsListener; record={}", record);
  }

  public void clear() {
    records.clear();
  }

  public static void main(String[] args) {
    SpringApplication.run(NatsListenerExample.class, args);
  }
}
