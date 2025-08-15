package ru.pleshkova.business.service;

import get_fuel_service.api.request.FuelRequest;
import get_fuel_service.api.response.FuelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pleshkova.business.errors.EntityNotFoundException;
import ru.pleshkova.business.mapper.FuelRecordMapper;
import ru.pleshkova.business.model.dto.RestResponse;
import ru.pleshkova.business.model.entity.FuelRecord;
import ru.pleshkova.business.model.entity.Vehicle;
import ru.pleshkova.business.model.repository.FuelRecordRepository;
import ru.pleshkova.business.model.repository.VehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FuelService {

    private final FuelRecordRepository fuelRecordRepository;
    private final VehicleRepository vehicleRepository;
    private final FuelRecordMapper fuelRecordMapper;

    public RestResponse doSomeStuff(String attribute) {
        return new RestResponse(attribute);
    }

    public FuelResponse.CreateFuelResponse createFuel(FuelRequest.CreateFuelRequest request) {
        final var vehicleId = request.getVehicleInternalId();
        final Vehicle vehicle = vehicleRepository.findByInternalId(UUID.fromString(vehicleId))
                .orElseThrow(() -> new EntityNotFoundException(Vehicle.class, vehicleId));
        final var fuel = fuelRecordMapper.mapToFuelEntity(request);
        fuel.setVehicle(vehicle);
        FuelRecord saved = fuelRecordRepository.save(fuel);

        return FuelResponse.CreateFuelResponse.newBuilder()
                .setInternalId(String.valueOf(saved.getInternalId()))
                .build();
    }

    @Transactional(readOnly = true)
    public FuelResponse.GetFuelResponse getFuel(FuelRequest.GetFuelRequest request) {
        final var response = FuelResponse.GetFuelResponse.newBuilder();
        List<FuelRecord> fuels = new ArrayList<>();
        switch (request.getFuelIdentifierCase()) {
            case VEHICLEINTERNALID -> fuels.addAll(fuelRecordRepository.findByVehicleInternalId(UUID.fromString(request.getVehicleInternalId())));
            case FUELINTERNALID -> fuelRecordRepository.findByInternalId(UUID.fromString(request.getFuelInternalId()))
                        .ifPresent(fuels::add);
        }
        fuels.forEach(fuel -> response.addFuelRecords(fuelRecordMapper.mapToGetFuelRecord(fuel)));
        return response.build();
    }

}
