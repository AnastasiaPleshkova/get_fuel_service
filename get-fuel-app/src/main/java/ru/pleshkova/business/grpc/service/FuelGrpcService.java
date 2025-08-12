package ru.pleshkova.business.grpc.service;

import get_fuel_service.api.request.FuelRequest;
import get_fuel_service.api.request.VehicleRequest;
import get_fuel_service.api.response.FuelResponse;
import get_fuel_service.api.response.VehicleResponse;
import get_fuel_service.api.service.FuelServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.pleshkova.business.service.FuelService;
import ru.pleshkova.business.service.VehicleService;
import ru.pleshkova.infrastructure.grpc.GrpcExecutor;

@GrpcService
@RequiredArgsConstructor
public class FuelGrpcService extends FuelServiceGrpc.FuelServiceImplBase {

    private final FuelService fuelService;
    private final VehicleService vehicleService;

    @Override
    @PreAuthorize("hasRole('ACCESS')")
    public void createVehicle(final VehicleRequest.CreateVehicleRequest request,
                              final StreamObserver<VehicleResponse.CreateVehicleResponse> responseObserver) {
        GrpcExecutor.create(vehicleService::createVehicle).execute(request, responseObserver);

    }

    @Override
    @PreAuthorize("hasRole('ACCESS')")
    public void getVehicle(final VehicleRequest.GetVehicleRequest request,
                           final StreamObserver<VehicleResponse.GetVehicleResponse> responseObserver) {
        GrpcExecutor.create(vehicleService::getVehicle).execute(request, responseObserver);

    }

    @Override
    @PreAuthorize("hasRole('ACCESS')")
    public void createFuel(final FuelRequest.CreateFuelRequest request,
                           final StreamObserver<FuelResponse.CreateFuelResponse> responseObserver) {
        GrpcExecutor.create(fuelService::createFuel).execute(request, responseObserver);

    }

    @Override
    @PreAuthorize("hasRole('ACCESS')")
    public void getFuel(final FuelRequest.GetFuelRequest request,
                        final StreamObserver<FuelResponse.GetFuelResponse> responseObserver) {
        GrpcExecutor.create(fuelService::getFuel).execute(request, responseObserver);

    }

//    Another example of grpc service
//    @Override
//    @PreAuthorize("hasRole('ACCESS')")
//    public void getVehicle(final VehicleRequest.CreateVehicleRequest request,
//                          final StreamObserver<VehicleResponse.CreateVehicleResponse> responseObserver) {
//        var response = VehicleResponse.CreateVehicleResponse.newBuilder();
//        try {
//            response.mergeFrom(service.createVehicle(request));
//        } catch (Exception e) {
//            log.error("error");
//        } finally {
//            responseObserver.onNext(response.build());
//            responseObserver.onCompleted();
//        }
//
//    }

}
