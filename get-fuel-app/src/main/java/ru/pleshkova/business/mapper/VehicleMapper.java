package ru.pleshkova.business.mapper;

import get_fuel_service.api.request.VehicleRequest;
import get_fuel_service.api.response.VehicleResponse;
import org.springframework.stereotype.Component;
import ru.pleshkova.business.model.entity.Vehicle;

@Component
public class VehicleMapper {

    public VehicleResponse.GetVehicle mapToGetVehicle(Vehicle entity) {
        return VehicleResponse.GetVehicle.newBuilder()
                .setInternalId(String.valueOf(entity.getInternalId()))
                .setVin(entity.getVin())
                .build();
    }

    public Vehicle mapToVehicleEntity(VehicleRequest.CreateVehicleRequest request) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVin(request.getVin());
        return vehicle;
    }
}
