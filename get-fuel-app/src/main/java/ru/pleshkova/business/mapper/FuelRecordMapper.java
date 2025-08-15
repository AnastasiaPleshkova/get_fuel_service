package ru.pleshkova.business.mapper;

import get_fuel_service.api.request.FuelRequest;
import get_fuel_service.api.response.FuelResponse;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;
import ru.pleshkova.business.model.entity.FuelRecord;

import java.time.LocalDateTime;

import static ru.pleshkova.business.utils.DateTimeUtils.localDateTime;
import static ru.pleshkova.business.utils.DateTimeUtils.timestamp;

@Component
public class FuelRecordMapper {

    public FuelRecord mapToFuelEntity(FuelRequest.CreateFuelRequest request) {
        FuelRecord fuel = new FuelRecord();

        fuel.setFuelLitres(String.valueOf(request.getLitres()));
        fuel.setMileage(String.valueOf(request.getMileage()));

        if (!StringUtils.isEmpty(request.getConsumptionByCar())) {
            fuel.setFuelConsumptionByVehicle(request.getConsumptionByCar());
        }

        LocalDateTime time = localDateTime(request.getCreatedTime());
        fuel.setRecordDate(time);


        return fuel;
    }

    public FuelResponse.GetFuelRecord mapToGetFuelRecord(FuelRecord entity) {
        return FuelResponse.GetFuelRecord.newBuilder()
                .setVehicleInternalId(String.valueOf(entity.getVehicle().getInternalId()))
                .setFuelInternalId(String.valueOf(entity.getInternalId()))
                .setMileage(entity.getMileage())
                .setLitres(entity.getFuelLitres())
                .setCreatedTime(timestamp(entity.getCreatedDate()))
                .build();
    }

}
