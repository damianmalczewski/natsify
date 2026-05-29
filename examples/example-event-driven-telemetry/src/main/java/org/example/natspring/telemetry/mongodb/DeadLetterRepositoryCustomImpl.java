package org.example.natspring.telemetry.mongodb;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class DeadLetterRepositoryCustomImpl implements DeadLetterRepositoryCustom {

  private final MongoOperations mongoOperations;

  public DeadLetterRepositoryCustomImpl(MongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public void upsertByStreamId(DeadLetterDocument deadLetter) {
    mongoOperations.upsert(
        Query.query(Criteria.where("streamId").is(deadLetter.getStreamId())),
        new Update()
            .setOnInsert("_id", new ObjectId().toHexString())
            .setOnInsert("streamId", deadLetter.getStreamId())
            .set("originalSubject", deadLetter.getOriginalSubject())
            .set("rawPayload", deadLetter.getRawPayload())
            .set("reason", deadLetter.getReason())
            .set("originalStream", deadLetter.getOriginalStream())
            .set("originalDurable", deadLetter.getOriginalDurable())
            .setOnInsert("deadLetteredAt", deadLetter.getDeadLetteredAt()),
        DeadLetterDocument.class);
  }
}
