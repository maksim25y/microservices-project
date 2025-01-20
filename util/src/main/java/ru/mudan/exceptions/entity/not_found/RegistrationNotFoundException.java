package ru.mudan.exceptions.entity.not_found;

import ru.mudan.exceptions.base.ApplicationNotFoundException;

public class RegistrationNotFoundException extends ApplicationNotFoundException {
    public RegistrationNotFoundException(Long registrationId) {
        super("registration.not.found", new Object[]{registrationId});
    }
}
