package org.example.natspring.telemetry.mongodb;

public interface DeviceEventRepositoryCustom {

  void upsertByEventId(DeviceEventDocument deviceEvent);
}
