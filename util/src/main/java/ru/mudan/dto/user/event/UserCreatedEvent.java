package ru.mudan.dto.user.event;

import lombok.Builder;

@Builder
public record UserCreatedEvent(
        String firstname,
        String lastname,
        String email
) {
}
