package org.example.natspring.telemetry.mongodb;

import java.time.Instant;
import org.jspecify.annotations.NullUnmarked;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@NullUnmarked
@Document("device_infos")
public class DeviceInfoDocument {

  @Id
  @Field("_id")
  private String id;

  @Indexed(name = "deviceId_unique", unique = true)
  @Field("deviceId")
  private String deviceId;

  @Field("totalEvents")
  private long totalEvents;

  @Field("lastActivityAt")
  private Instant lastActivityAt;

  /** For use by Spring Data MongoDB. */
  protected DeviceInfoDocument() {}

  public String getId() {
    return id;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public long getTotalEvents() {
    return totalEvents;
  }

  public Instant getLastActivityAt() {
    return lastActivityAt;
  }
}
