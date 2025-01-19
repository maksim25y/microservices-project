package ru.mudan.exceptions.entity.not_found;

import ru.mudan.exceptions.base.ApplicationNotFoundException;

public final class UserNotFoundException extends ApplicationNotFoundException {

    public UserNotFoundException(String email) {
        super("user.not.found", new Object[]{email});
    }
}
