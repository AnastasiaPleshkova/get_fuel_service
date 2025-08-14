package ru.pleshkova.business.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "vehicle")
public class Vehicle extends BaseEntity {

    private String vin;

    private UUID internalId;
    @PrePersist
    public void prePersist() {
        this.internalId = UUID.randomUUID();
    }

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FuelRecord> fuelRecords = new ArrayList<>();

}
