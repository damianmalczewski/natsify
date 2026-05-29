package org.example.natspring.telemetry.nats;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;
import org.springframework.stereotype.Component;

@Component
public class StreamSequenceSupport {

  public String build(String streamName, long sequence) {
    return hash(streamName) + "-" + String.format("%012d", sequence);
  }

  private String hash(String streamName) {
    CRC32 crc = new CRC32();
    crc.update(streamName.getBytes(StandardCharsets.UTF_8));
    return String.format("%08x", crc.getValue());
  }
}
