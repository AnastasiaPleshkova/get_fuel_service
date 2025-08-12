package ru.pleshkova.business.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pleshkova.business.model.dto.RestResponse;
import ru.pleshkova.business.service.FuelService;

@RestController
@RequiredArgsConstructor
public class GetFuelRestController {

    private final FuelService service;

    @GetMapping("/api/reference/search")
    public ResponseEntity<RestResponse> getData(@RequestParam(required = false) String attribute) {
        final var response = service.doSomeStuff(attribute);
        return ResponseEntity.ok(response);
    }
}
