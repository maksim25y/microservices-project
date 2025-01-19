package ru.mudan.exceptions.base;

import org.springframework.http.HttpStatus;

public abstract class ApplicationNotFoundException extends ApplicationRuntimeException {

    public ApplicationNotFoundException(String message, Object[] args) {
        super(message, HttpStatus.NOT_FOUND, args);
    }
}
