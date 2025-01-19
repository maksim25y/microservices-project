package ru.mudan.exceptions.entity.already_exists;

import ru.mudan.exceptions.base.ApplicationConflictException;

public final class UserAlreadyExistsException extends ApplicationConflictException {

    public UserAlreadyExistsException(String email) {
        super("user.email.is_busy", new Object[]{email});
    }
}
