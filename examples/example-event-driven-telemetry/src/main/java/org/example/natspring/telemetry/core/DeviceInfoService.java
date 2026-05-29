package org.example.natspring.telemetry.core;

import java.time.Clock;
import java.util.Optional;
import org.example.natspring.telemetry.core.model.DeviceInfoModel;
import org.example.natspring.telemetry.core.model.IncrementEventCountCommand;
import org.example.natspring.telemetry.core.model.RecordActivityCommand;
import org.example.natspring.telemetry.mongodb.DeviceInfoDocument;
import org.example.natspring.telemetry.mongodb.DeviceInfoRepository;
import org.springframework.stereotype.Service;

@Service
public class DeviceInfoService {

  private final DeviceInfoRepository deviceInfoRepository;
  private final Clock clock;

  public DeviceInfoService(DeviceInfoRepository deviceInfoRepository, Clock clock) {
    this.deviceInfoRepository = deviceInfoRepository;
    this.clock = clock;
  }

  public void incrementEventCount(IncrementEventCountCommand command) {
    deviceInfoRepository.incrementTotalEvents(command.deviceId());
  }

  public void recordActivity(RecordActivityCommand command) {
    deviceInfoRepository.updateLastActivity(command.deviceId(), clock.instant());
  }

  public Optional<DeviceInfoModel> findByDeviceId(String deviceId) {
    return deviceInfoRepository.findByDeviceId(deviceId).map(this::toDeviceInfoModel);
  }

  private DeviceInfoModel toDeviceInfoModel(DeviceInfoDocument entity) {
    return new DeviceInfoModel(
        entity.getDeviceId(), entity.getTotalEvents(), entity.getLastActivityAt());
  }
}
