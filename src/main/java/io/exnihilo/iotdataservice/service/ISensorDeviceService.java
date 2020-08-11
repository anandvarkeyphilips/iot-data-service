package io.exnihilo.iotdataservice.service;

import io.exnihilo.iotdataservice.model.SensorDevice;
import io.exnihilo.iotdataservice.model.SensorDeviceDataFilter;
import io.exnihilo.iotdataservice.model.SensorData;
import org.springframework.stereotype.Service;

/**
 * LocationService helps with Here Maps APIs as per business logic.
 *
 * @author Anand Varkey Philips
 * @date 10/08/2020
 * @since 1.0.0.RELEASE
 */
@Service
public interface ISensorDeviceService {

  SensorDevice postSensorDevice(SensorDevice sensorDevice);

  SensorDevice getSensorData(String deviceId);

  SensorData postSensorData(String deviceId,SensorData sensorData);

  SensorDeviceDataFilter storeQueryFilter(String deviceId, SensorDeviceDataFilter sensorDeviceDataFilter);

  SensorDevice getSensorDataFiltered(String deviceId);

}
