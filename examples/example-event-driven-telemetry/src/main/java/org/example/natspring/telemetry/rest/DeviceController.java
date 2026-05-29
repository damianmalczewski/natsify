package org.example.natspring.telemetry.rest;

import org.example.natspring.telemetry.core.DeviceEventService;
import org.example.natspring.telemetry.core.DeviceInfoService;
import org.example.natspring.telemetry.core.model.ContentModel;
import org.example.natspring.telemetry.core.model.DeviceEventModel;
import org.example.natspring.telemetry.core.model.DeviceInfoModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/devices")
public class DeviceController {

  private final DeviceInfoService deviceInfoService;
  private final DeviceEventService deviceEventService;

  public DeviceController(
      DeviceInfoService deviceInfoService, DeviceEventService deviceEventService) {
    this.deviceInfoService = deviceInfoService;
    this.deviceEventService = deviceEventService;
  }

  @GetMapping("/{id}")
  public DeviceInfoModel getDevice(@PathVariable String id) {
    return deviceInfoService
        .findByDeviceId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/{id}/history-events")
  public ContentModel<DeviceEventModel> getDeviceHistory(@PathVariable String id) {
    return new ContentModel<>(deviceEventService.findByDeviceId(id));
  }
}
