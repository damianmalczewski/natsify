package org.example.natspring.telemetry.mongodb;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface DeviceEventRepository
    extends MongoRepository<DeviceEventDocument, String>, DeviceEventRepositoryCustom {

  @Query(value = "{ deviceId : ?0 }", sort = "{ receivedAt : -1 }")
  List<DeviceEventDocument> findByDeviceId(String deviceId);
}
