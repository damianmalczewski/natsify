package org.example.natspring.telemetry.mongodb;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeadLetterRepository
    extends MongoRepository<DeadLetterDocument, String>, DeadLetterRepositoryCustom {

  Optional<DeadLetterDocument> findByStreamId(String streamId);
}
