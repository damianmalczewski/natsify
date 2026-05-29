package org.example.natspring.telemetry.mongodb;

import java.time.Instant;

public interface DeviceInfoRepositoryCustom {

  void incrementTotalEvents(String deviceId);

  void updateLastActivity(String deviceId, Instant at);
}
