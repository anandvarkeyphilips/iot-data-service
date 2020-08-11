
package io.exnihilo.iotdataservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class SensorDeviceDataFilter implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer sensorDeviceDataFilterId;

    Integer greaterThan;

    @OneToOne
    private SensorDevice sensorDevice;
}
