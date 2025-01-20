package ru.mudan.exceptions.base;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends ApplicationRuntimeException {

    public AuthorizationException(String email) {
        super("unauthorized", HttpStatus.UNAUTHORIZED, new Object[] {email});
    }
}
