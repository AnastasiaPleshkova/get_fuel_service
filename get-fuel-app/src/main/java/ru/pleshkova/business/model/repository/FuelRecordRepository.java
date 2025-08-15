package ru.pleshkova.business.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pleshkova.business.model.entity.FuelRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FuelRecordRepository extends JpaRepository<FuelRecord, Long> {

    Optional<FuelRecord> findByInternalId(UUID internalId);

    @Query("SELECT f FROM FuelRecord f " +
            "JOIN FETCH f.vehicle v " +
            "WHERE v.internalId = :internalId")
    List<FuelRecord> findByVehicleInternalId(UUID internalId);

}
