package ru.mudan.dto.user.auth;

import lombok.Builder;

@Builder
public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
