package org.example.natspring.telemetry.core.model;

import java.time.Instant;
import org.jspecify.annotations.Nullable;

public record DeviceInfoModel(String id, long totalEvents, @Nullable Instant lastActivityAt) {}
