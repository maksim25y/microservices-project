package ru.mudan.dto.user.event;

import lombok.Builder;

@Builder
public record UserUpdatingEvent(
        String email,
        String firstname,
        String lastname
) {
}
