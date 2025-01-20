package ru.mudan.dto.user.auth;

import lombok.Builder;

@Builder
public record AuthRequest(
        String username,
        String password) {
}
