package ru.pleshkova.business.service;

import get_fuel_service.api.request.VehicleRequest;
import get_fuel_service.api.response.VehicleResponse;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pleshkova.business.mapper.VehicleMapper;
import ru.pleshkova.business.model.entity.Vehicle;
import ru.pleshkova.business.model.repository.VehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    public VehicleResponse.CreateVehicleResponse createVehicle(VehicleRequest.CreateVehicleRequest request) {
        if (StringUtils.isEmpty(request.getVin())) {
            throw new RuntimeException();
        }
        final Vehicle saved = vehicleRepository.save(vehicleMapper.mapToVehicleEntity(request));

        return VehicleResponse.CreateVehicleResponse.newBuilder()
                .setInternalId(String.valueOf(saved.getInternalId()))
                .build();
    }

    public VehicleResponse.GetVehicleResponse getVehicle(VehicleRequest.GetVehicleRequest request) {
        final List<Vehicle> vehicles = new ArrayList<>();
        switch (request.getVehicleIdentifierCase()) {
            case VIN -> vehicles.addAll(vehicleRepository.findByVin(request.getVin()));
            case INTERNALID -> vehicleRepository.findByInternalId(UUID.fromString(request.getInternalId()))
                                                    .ifPresent(vehicles::add);
        }
        final var response = VehicleResponse.GetVehicleResponse.newBuilder();
        vehicles.forEach(vehicle -> response.addVehicles(vehicleMapper.mapToGetVehicle(vehicle)));
        return response.build();
    }

}
