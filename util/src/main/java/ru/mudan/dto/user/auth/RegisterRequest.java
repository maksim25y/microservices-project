package ru.mudan.dto.user.auth;

import lombok.Builder;

@Builder
public record RegisterRequest(
        String firstname,
        String lastname,
        String email,
        String password
) {
}
