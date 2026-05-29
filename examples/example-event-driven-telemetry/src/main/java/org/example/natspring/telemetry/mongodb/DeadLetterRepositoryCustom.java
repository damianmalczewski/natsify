package org.example.natspring.telemetry.mongodb;

public interface DeadLetterRepositoryCustom {

  void upsertByStreamId(DeadLetterDocument deadLetter);
}
