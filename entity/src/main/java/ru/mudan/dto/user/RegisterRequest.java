package ru.mudan.dto.user;

import lombok.Builder;

@Builder
public record RegisterRequest(
        String username,
        String firstname,
        String lastname,
        String email
) {
}
