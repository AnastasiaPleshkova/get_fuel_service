package ru.pleshkova.business.service;

import get_fuel_service.api.request.VehicleRequest;
import get_fuel_service.api.response.VehicleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pleshkova.business.model.repository.VehicleRepository;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleResponse.CreateVehicleResponse createVehicle(VehicleRequest.CreateVehicleRequest request) {
        return VehicleResponse.CreateVehicleResponse.newBuilder().build();
    }

    public VehicleResponse.GetVehicleResponse getVehicle(VehicleRequest.GetVehicleRequest request) {
        return VehicleResponse.GetVehicleResponse.newBuilder().build();
    }

}
