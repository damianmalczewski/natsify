package org.example.natspring.telemetry.core;

import java.util.List;
import org.example.natspring.telemetry.core.model.DeviceEventCommand;
import org.example.natspring.telemetry.core.model.DeviceEventModel;
import org.example.natspring.telemetry.mongodb.DeviceEventDocument;
import org.example.natspring.telemetry.mongodb.DeviceEventRepository;
import org.springframework.stereotype.Service;

@Service
public class DeviceEventService {

  private final DeviceEventRepository deviceEventRepository;

  public DeviceEventService(DeviceEventRepository deviceEventRepository) {
    this.deviceEventRepository = deviceEventRepository;
  }

  public void persistEvent(DeviceEventCommand command) {
    DeviceEventDocument deviceEvent =
        new DeviceEventDocument(
            command.eventId(),
            command.deviceId(),
            command.type(),
            command.payload(),
            command.timestamp(),
            command.receivedAt());
    deviceEventRepository.upsertByEventId(deviceEvent);
  }

  public List<DeviceEventModel> findByDeviceId(String deviceId) {
    return deviceEventRepository.findByDeviceId(deviceId).stream()
        .map(this::toDeviceEventModel)
        .toList();
  }

  private DeviceEventModel toDeviceEventModel(DeviceEventDocument entity) {
    return new DeviceEventModel(
        entity.getEventId(),
        entity.getDeviceId(),
        entity.getType(),
        entity.getPayload(),
        entity.getTimestamp(),
        entity.getReceivedAt());
  }
}
