package ru.mudan.dto.user.auth;

import lombok.Builder;
import ru.mudan.dto.user.enums.RegistrationStatus;

@Builder
public record RegistrationResponse(
    RegistrationStatus status,
    Long registrationId
) {
}
