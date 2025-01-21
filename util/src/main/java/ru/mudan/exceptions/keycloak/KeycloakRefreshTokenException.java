package ru.mudan.exceptions.keycloak;

import org.springframework.http.HttpStatus;
import ru.mudan.exceptions.base.ApplicationRuntimeException;

public class KeycloakRefreshTokenException extends ApplicationRuntimeException {
    public KeycloakRefreshTokenException() {
        super("refresh.token.keycloak.error", HttpStatus.BAD_REQUEST, new Object[]{});
    }
}
