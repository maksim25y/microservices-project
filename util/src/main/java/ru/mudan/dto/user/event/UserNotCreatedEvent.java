package ru.mudan.dto.user.event;

import lombok.Builder;

@Builder
public record UserNotCreatedEvent(
        String firstname,
        String lastname,
        String email
) {
}
