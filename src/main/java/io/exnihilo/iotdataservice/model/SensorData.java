
package io.exnihilo.iotdataservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class SensorData implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer sensorDataId;

    private Integer data;

    @ManyToOne
    private SensorDevice sensorDevice;
}
