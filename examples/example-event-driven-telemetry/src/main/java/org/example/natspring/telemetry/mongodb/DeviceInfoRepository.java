package org.example.natspring.telemetry.mongodb;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceInfoRepository
    extends MongoRepository<DeviceInfoDocument, String>, DeviceInfoRepositoryCustom {

  Optional<DeviceInfoDocument> findByDeviceId(String deviceId);
}
