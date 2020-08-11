package io.exnihilo.iotdataservice.controller;

import io.exnihilo.iotdataservice.model.SensorDevice;
import io.exnihilo.iotdataservice.model.SensorDeviceDataFilter;
import io.exnihilo.iotdataservice.model.SensorData;
import io.exnihilo.iotdataservice.service.ISensorDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController()
@RequestMapping("/device")
@Api(tags = {"Data Service"})
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully completed the operation"),
        @ApiResponse(code = 401, message = "You are not authenticated properly to view the resource!"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden!"),
        @ApiResponse(code = 404, message = "Page you are looking for is not available right now!")})
public class SensorDeviceController {

    @Autowired
    private ISensorDeviceService locationService;

    @PostMapping
    @ApiOperation(value = "API for creating Sensor Device", notes = "This API creates sensor device")
    public ResponseEntity<SensorDevice> postSensorDevice(@RequestBody SensorDevice sensorDevice) {
        return new ResponseEntity<>(locationService.postSensorDevice(sensorDevice), HttpStatus.OK);
    }

    @GetMapping(value = "/{deviceId}/data")
    public ResponseEntity<SensorDevice> getSensorData(@PathVariable("deviceId") String deviceId) {
        return new ResponseEntity<>(locationService.getSensorData(deviceId), HttpStatus.OK);
    }

    @PostMapping(value = "/{deviceId}/data")
    @ApiOperation(value = "API for getting POI", notes = "This API validates JSON data input.")
    public ResponseEntity<SensorData> postSensorData(@PathVariable("deviceId") String deviceId,@RequestBody SensorData sensorData) {
        return new ResponseEntity<>(locationService.postSensorData(deviceId,sensorData), HttpStatus.OK);
    }

    @PostMapping(value = "/{deviceId}/filter")
    public ResponseEntity<SensorDeviceDataFilter> postSensorDeviceDataFilter(@PathVariable("deviceId") String deviceId, @RequestBody SensorDeviceDataFilter sensorDeviceDataFilter) {
        return new ResponseEntity<>(locationService.storeQueryFilter(deviceId, sensorDeviceDataFilter), HttpStatus.OK);
    }

    @GetMapping(value = "/{deviceId}/data/filtered")
    public ResponseEntity<SensorDevice> getSensorDataFiltered(@PathVariable("deviceId") String deviceId) {
        return new ResponseEntity<>(locationService.getSensorDataFiltered(deviceId), HttpStatus.OK);
    }
}
