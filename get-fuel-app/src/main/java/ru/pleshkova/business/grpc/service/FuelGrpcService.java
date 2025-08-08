package ru.pleshkova.business.grpc.service;

import get_fuel_service.api.request.CarRequest;
import get_fuel_service.api.response.CarResponse;
import get_fuel_service.api.service.FuelServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.pleshkova.business.service.SomeVeryImportantService;
import ru.pleshkova.infrastructure.grpc.GrpcExecutor;

@GrpcService
@RequiredArgsConstructor
public class FuelGrpcService extends FuelServiceGrpc.FuelServiceImplBase {

    private final SomeVeryImportantService service;

    @Override
    @PreAuthorize("hasRole('ACCESS')")
    public void createCar(final CarRequest.CreateCarRequest request,
                          final StreamObserver<CarResponse.CreateCarResponse> responseObserver) {
        GrpcExecutor.create(service::createCar).execute(request, responseObserver);

    }
}
