package ru.pleshkova.business.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pleshkova.business.model.entity.FuelRecord;

import java.util.Optional;
import java.util.UUID;

public interface FuelRecordRepository extends JpaRepository<FuelRecord, Long> {

    Optional<FuelRecord> findByInternalId(UUID internalId);

}
