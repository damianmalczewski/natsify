package org.example.natspring.telemetry.nats.model;

import java.time.Instant;
import java.util.Map;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
public record DeviceEventMessage(
    String deviceId, String type, Map<String, Object> payload, Instant timestamp) {}
