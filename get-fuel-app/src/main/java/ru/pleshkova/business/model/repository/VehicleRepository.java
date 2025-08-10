package ru.pleshkova.business.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pleshkova.business.model.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
