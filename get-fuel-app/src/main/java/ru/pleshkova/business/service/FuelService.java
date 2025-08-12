package ru.pleshkova.business.service;

import get_fuel_service.api.request.FuelRequest;
import get_fuel_service.api.response.FuelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pleshkova.business.model.dto.RestResponse;
import ru.pleshkova.business.model.entity.FuelRecord;
import ru.pleshkova.business.model.repository.FuelRecordRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FuelService {

    private final FuelRecordRepository fuelRecordRepository;

    public RestResponse doSomeStuff(String attribute) {
        return new RestResponse(attribute);
    }

    public FuelResponse.CreateFuelResponse createFuel(FuelRequest.CreateFuelRequest request) {
        final var vehicleId = request.getVehicleId();
        FuelResponse.CreateFuelResponse.Builder response = FuelResponse.CreateFuelResponse.newBuilder();
        FuelRecord newRecord = new FuelRecord();
        // needed to map from request new enitity
        FuelRecord save = fuelRecordRepository.save(newRecord);
        return response.build();
    }

    public FuelResponse.GetFuelResponse getFuel(FuelRequest.GetFuelRequest request) {
        final var vehicleId = request.getVehicleId();
        final var response = FuelResponse.GetFuelResponse.newBuilder();
        List<FuelRecord> fuelRecords = fuelRecordRepository.findByVehicleId(vehicleId);
        // needed to map from request new enitity
        return response.build();
    }

}
