
package io.exnihilo.iotdataservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
public class SensorDevice implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer sensorDeviceId;

    String name;

    @OneToOne(cascade = CascadeType.ALL)
    private SensorDeviceDataFilter sensorDeviceDataFilter;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SensorData> sensorData;
}
