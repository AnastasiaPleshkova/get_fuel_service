package ru.pleshkova.infrastructure.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.google.protobuf.Message;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class GrpcExecutor<T extends Message, N extends Message> {

    private final Function<T, N> executor;

    public static <T extends Message, N extends Message> GrpcExecutor<T, N> create(final Function<T, N> executor) {
        return new GrpcExecutor<>(executor);
    }

    public final void execute(final T request, final StreamObserver<N> responseObserver) {
        responseObserver.onNext(executor.apply(request));
        responseObserver.onCompleted();
    }
}
