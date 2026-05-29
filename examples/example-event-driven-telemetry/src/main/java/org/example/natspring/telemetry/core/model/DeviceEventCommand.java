package org.example.natspring.telemetry.core.model;

import java.time.Instant;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public record DeviceEventCommand(
    String eventId,
    String deviceId,
    String type,
    @Nullable Map<String, Object> payload,
    Instant timestamp,
    Instant receivedAt) {}
