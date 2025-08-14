package ru.pleshkova.business.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pleshkova.business.model.entity.Vehicle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByVin(String vin);

    Optional<Vehicle> findByInternalId(UUID internalId);
}
