package ru.pleshkova.business.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "fuel_record")
public class FuelRecord extends BaseEntity {

    private String fuelLitres;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime recordDate;

    private String mileage;

    private String fuelConsumptionByVehicle;

    private UUID internalId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

}
