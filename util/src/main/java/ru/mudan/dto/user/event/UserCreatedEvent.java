package ru.mudan.dto.user.event;

public record UserCreatedEvent(
        String firstname,
        String lastname,
        String email,
        Long registrationId
) {
}
