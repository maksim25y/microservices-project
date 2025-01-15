package ru.mudan.dto.user;

import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String firstname,
        String lastname,
        String email
) {
}
