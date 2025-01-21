package ru.mudan.dto.user.event;

import lombok.Builder;

@Builder
public record UserCreatingEvent(
        String firstname,
        String lastname,
        String email
) {
}
