package ru.mudan.dto.user;

import lombok.Builder;

@Builder
public record UserUpdateRequest(
        String firstname,
        String lastname
) {
}
