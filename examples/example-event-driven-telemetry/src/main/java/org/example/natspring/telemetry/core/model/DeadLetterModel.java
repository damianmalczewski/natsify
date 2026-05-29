package org.example.natspring.telemetry.core.model;

import java.time.Instant;
import org.jspecify.annotations.Nullable;

public record DeadLetterModel(
    String id,
    @Nullable String originalSubject,
    String rawPayload,
    @Nullable String reason,
    @Nullable String originalStream,
    @Nullable String originalDurable,
    Instant deadLetteredAt) {}
