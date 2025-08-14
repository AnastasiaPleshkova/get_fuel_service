package ru.pleshkova.business.grpc.advice;

import com.google.protobuf.Any;
import com.google.rpc.Code;
import com.google.rpc.DebugInfo;
import com.google.rpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import ru.pleshkova.business.errors.EntityNotFoundException;

import java.util.Arrays;

@Slf4j
@GrpcAdvice
public class GrpcExceptionAdvice {

    @GrpcExceptionHandler(RuntimeException.class)
    public StatusRuntimeException handleException(final RuntimeException exception) {
        Status status = Status.newBuilder()
                .setCode(Code.UNKNOWN_VALUE)
                .setMessage(exception.getMessage())
                .addDetails(Any.pack(DebugInfo.newBuilder()
                        .setDetail(exception.getMessage())
                        .addAllStackEntries(Arrays.stream(exception.getStackTrace())
                                .map(StackTraceElement::toString)
                                .toList())
                        .build()))
                .build();

        log.error(exception.getMessage(), exception);
        return StatusProto.toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler(StatusRuntimeException.class)
    public StatusRuntimeException handleException(final StatusRuntimeException exception) {
        log.error(exception.getMessage(), exception);
        return exception;
    }

    @GrpcExceptionHandler(EntityNotFoundException.class)
    public StatusRuntimeException handleException(final EntityNotFoundException exception) {
        Status status = Status.newBuilder()
                .setCode(exception.getCode())
                .setMessage(exception.getMessage())
                .addDetails(Any.pack(DebugInfo.newBuilder()
                        .setDetail(exception.getMessage())
                        .addAllStackEntries(Arrays.stream(exception.getStackTrace())
                                .map(StackTraceElement::toString)
                                .toList())
                        .build()))
                .build();

        log.warn(exception.getMessage(), exception);
        return StatusProto.toStatusRuntimeException(status);
    }
}
