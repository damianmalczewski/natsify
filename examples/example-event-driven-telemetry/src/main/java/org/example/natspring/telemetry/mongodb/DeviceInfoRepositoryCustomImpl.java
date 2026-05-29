package org.example.natspring.telemetry.mongodb;

import java.time.Instant;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class DeviceInfoRepositoryCustomImpl implements DeviceInfoRepositoryCustom {

  private final MongoOperations mongoOperations;

  public DeviceInfoRepositoryCustomImpl(MongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public void incrementTotalEvents(String deviceId) {
    mongoOperations.upsert(
        Query.query(Criteria.where("deviceId").is(deviceId)),
        new Update()
            .setOnInsert("_id", new ObjectId().toHexString())
            .inc("totalEvents", 1)
            .setOnInsert("lastActivityAt", null),
        DeviceInfoDocument.class);
  }

  @Override
  public void updateLastActivity(String deviceId, Instant at) {
    mongoOperations.upsert(
        Query.query(Criteria.where("deviceId").is(deviceId)),
        new Update()
            .setOnInsert("_id", new ObjectId().toHexString())
            .set("lastActivityAt", at)
            .setOnInsert("totalEvents", 0L),
        DeviceInfoDocument.class);
  }
}
