package org.example.natspring.telemetry.mongodb;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class DeviceEventRepositoryCustomImpl implements DeviceEventRepositoryCustom {

  private final MongoOperations mongoOperations;

  public DeviceEventRepositoryCustomImpl(MongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public void upsertByEventId(DeviceEventDocument deviceEvent) {
    mongoOperations.upsert(
        Query.query(Criteria.where("eventId").is(deviceEvent.getEventId())),
        new Update()
            .setOnInsert("_id", new ObjectId().toHexString())
            .setOnInsert("eventId", deviceEvent.getEventId())
            .setOnInsert("deviceId", deviceEvent.getDeviceId())
            .setOnInsert("type", deviceEvent.getType())
            .setOnInsert("payload", deviceEvent.getPayload())
            .setOnInsert("timestamp", deviceEvent.getTimestamp())
            .setOnInsert("receivedAt", deviceEvent.getReceivedAt()),
        DeviceEventDocument.class);
  }
}
