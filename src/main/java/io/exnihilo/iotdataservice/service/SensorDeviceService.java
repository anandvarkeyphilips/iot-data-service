package io.exnihilo.iotdataservice.service;

import io.exnihilo.iotdataservice.constants.IotDataServiceConstants;
import io.exnihilo.iotdataservice.model.SensorData;
import io.exnihilo.iotdataservice.model.SensorDevice;
import io.exnihilo.iotdataservice.model.SensorDeviceDataFilter;
import io.exnihilo.iotdataservice.repository.SensorDeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * LocationService helps with Here Maps APIs as per business logic.
 *
 * @author Anand Varkey Philips
 * @date 10/08/2020
 * @since 1.0.0.RELEASE
 */
@Slf4j
@Service
public class SensorDeviceService implements ISensorDeviceService {

    @Autowired
    private SensorDeviceRepository sensorDeviceRepository;

    @Override
    public SensorDevice postSensorDevice(SensorDevice sensorDevice) {
        if (sensorDevice.getSensorDeviceId() == null || !sensorDeviceRepository.existsById(sensorDevice.getSensorDeviceId())) {
            return sensorDeviceRepository.save(sensorDevice);
        } else {
            log.warn(IotDataServiceConstants.ENTITY_ALREADY_FOUND_EXCEPTION_MESSAGE, sensorDevice.getSensorDeviceId());
            throw new EntityNotFoundException("Entity with id:" + sensorDevice.getSensorDeviceId() + " was already found");
        }
    }

    @Override
    public SensorDevice getSensorData(String deviceId) {
        if (sensorDeviceRepository.existsById(Integer.parseInt(deviceId))) {
            return sensorDeviceRepository.findById(Integer.parseInt(deviceId)).get();
        } else {
            log.warn(IotDataServiceConstants.ENTITY_NOT_FOUND_EXCEPTION_MESSAGE, deviceId);
            throw new EntityNotFoundException("Entity with id:" + deviceId + " was not found");
        }
    }

    @Override
    public SensorData postSensorData(String deviceId,SensorData sensorData) {
        if (sensorDeviceRepository.existsById(Integer.parseInt(deviceId))) {
            SensorDevice sensorDevice = sensorDeviceRepository.findById(Integer.parseInt(deviceId)).get();
            List<SensorData> sensorDataList = sensorDevice.getSensorData();
            sensorDataList.add(sensorData);
            sensorDeviceRepository.save(sensorDevice);
            return sensorData;
        } else {
            log.warn(IotDataServiceConstants.ENTITY_NOT_FOUND_EXCEPTION_MESSAGE, deviceId);
            throw new EntityNotFoundException("Entity with id:" + deviceId + " was not found");
        }
    }

    @Override
    public SensorDeviceDataFilter storeQueryFilter(String deviceId, SensorDeviceDataFilter sensorDeviceDataFilter) {
        if (sensorDeviceRepository.existsById(Integer.parseInt(deviceId))) {
            SensorDevice sensorDevice = sensorDeviceRepository.findById(Integer.parseInt(deviceId)).get();
            sensorDevice.setSensorDeviceDataFilter(sensorDeviceDataFilter);
            sensorDeviceRepository.save(sensorDevice);
            return sensorDeviceDataFilter;
        } else {
            log.warn(IotDataServiceConstants.ENTITY_NOT_FOUND_EXCEPTION_MESSAGE, deviceId);
            throw new EntityNotFoundException("Entity with id:" + deviceId + " was not found");
        }
    }

    @Override
    public SensorDevice getSensorDataFiltered(String deviceId) {
        if (sensorDeviceRepository.existsById(Integer.parseInt(deviceId))) {
            SensorDevice sensorDevice = sensorDeviceRepository.findById(Integer.parseInt(deviceId)).get();
            if(sensorDevice.getSensorDeviceDataFilter().getGreaterThan() != null){
                List<SensorData> sensorDataList = sensorDevice.getSensorData();
                final List<SensorData> filteredList = IntStream.range(0, sensorDataList.size() - 1)
                        .filter(i -> sensorDataList.get(i).getData() > sensorDevice.getSensorDeviceDataFilter().getGreaterThan())
                        .mapToObj(sensorDataList::get)
                        .collect(Collectors.toList());
                sensorDevice.setSensorData(filteredList);
                return sensorDevice;
            }
            return sensorDevice;
        } else {
            log.warn(IotDataServiceConstants.ENTITY_NOT_FOUND_EXCEPTION_MESSAGE, deviceId);
            throw new EntityNotFoundException("Entity with id:" + deviceId + " was not found");
        }
    }
}
