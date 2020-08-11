package io.exnihilo.iotdataservice.repository;

import io.exnihilo.iotdataservice.model.SensorData;
import io.exnihilo.iotdataservice.model.SensorDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorDeviceRepository extends JpaRepository<SensorDevice, Integer> {


}
