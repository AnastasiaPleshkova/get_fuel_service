package ru.pleshkova.business.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pleshkova.business.model.dto.FuelCreateRequest;
import ru.pleshkova.business.model.dto.RestResponse;
import ru.pleshkova.business.service.FuelService;

@RestController
@RequiredArgsConstructor
public class GetFuelRestController {

    private final FuelService service;

    @GetMapping("/v1/api/reference/search")
    public ResponseEntity<RestResponse> getData(@RequestParam(required = false) String attribute) {
        final var response = service.doSomeStuff(attribute);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/api/fuel")
    public ResponseEntity<RestResponse> createFuel(@RequestParam(required = false) String litres, @RequestParam(required = false) String mileage) {
        final var response = service.sendFuel(new FuelCreateRequest(litres, mileage));
        return ResponseEntity.ok(response);
    }
}
