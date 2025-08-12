package ru.pleshkova.business.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pleshkova.business.model.entity.FuelRecord;

import java.util.List;

public interface FuelRecordRepository extends JpaRepository<FuelRecord, Long> {

    List<FuelRecord> findByVehicleId(Long vehicleId);

}
