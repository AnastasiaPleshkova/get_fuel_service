package ru.pleshkova.business.service;

import get_fuel_service.api.request.FuelRequest;
import get_fuel_service.api.response.FuelResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pleshkova.business.errors.EntityNotFoundException;
import ru.pleshkova.business.mapper.FuelRecordMapper;
import ru.pleshkova.business.model.dto.FuelCreateRequest;
import ru.pleshkova.business.model.dto.FuelMessage;
import ru.pleshkova.business.model.dto.RestResponse;
import ru.pleshkova.business.model.entity.FuelRecord;
import ru.pleshkova.business.model.entity.Vehicle;
import ru.pleshkova.business.model.repository.FuelRecordRepository;
import ru.pleshkova.business.model.repository.VehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FuelService {

    private final FuelRecordRepository fuelRecordRepository;
    private final VehicleRepository vehicleRepository;
    private final FuelRecordMapper fuelRecordMapper;
    private final KafkaTemplate<String, FuelMessage> exporter;

    public RestResponse doSomeStuff(String attribute) {
        return new RestResponse(attribute);
    }

    public RestResponse sendFuel(final FuelCreateRequest request)  {
        FuelMessage kafkaMessage = fuelRecordMapper.getKafkaMessage(request);
        try {
            CompletableFuture<SendResult<String, FuelMessage>> sendResultCompletableFuture = exporter.sendDefault(kafkaMessage);
            SendResult<String, FuelMessage> stringFuelMessageSendResult = sendResultCompletableFuture.get();
            log.info("Сообщение было отправлено, ключ: {}, partition: {}",
                    stringFuelMessageSendResult.getProducerRecord().key(),
                    stringFuelMessageSendResult.getRecordMetadata().partition());
        } catch (Exception ex) {
            log.error("There is an error occurs during send message to kafka", ex);
        }

        return new RestResponse("ok");
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
