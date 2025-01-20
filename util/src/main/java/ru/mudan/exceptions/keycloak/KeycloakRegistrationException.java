package ru.mudan.exceptions.keycloak;

import org.springframework.http.HttpStatus;
import ru.mudan.exceptions.base.ApplicationRuntimeException;

public class KeycloakRegistrationException extends ApplicationRuntimeException {
    public KeycloakRegistrationException(String email) {
        super("registration.keycloak.error", HttpStatus.BAD_REQUEST, new Object[] {email});
    }
}
