package ru.pleshkova.business.errors;

import lombok.Getter;

import static com.google.rpc.Code.NOT_FOUND;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Entity %s with identifier %s not found";

    private final int code;
    private final String message;

    public EntityNotFoundException(final Class<?> entityClass, final String identifier) {
        this.code = NOT_FOUND.getNumber();
        this.message = MESSAGE_TEMPLATE.formatted(entityClass.getName(), identifier);
    }
}
