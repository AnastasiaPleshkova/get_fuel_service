package ru.pleshkova.business.service;

import get_fuel_service.api.request.CarRequest;
import get_fuel_service.api.response.CarResponse;
import org.springframework.stereotype.Service;
import ru.pleshkova.business.model.dto.RestResponse;

@Service
public class SomeVeryImportantService {

    public RestResponse doSomeStuff(String attribute) {
        return new RestResponse(attribute);
    }

    public CarResponse.CreateCarResponse createCar(CarRequest.CreateCarRequest request) {
        return CarResponse.CreateCarResponse.newBuilder().build();
    }
}
