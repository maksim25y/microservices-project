package ru.mudan.dto;

import lombok.Builder;

@Builder
public record RegisterRequest(
        String username,
        String email,
        String firstname,
        String lastname,
        String password
) {
}
