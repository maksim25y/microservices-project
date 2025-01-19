package ru.mudan.dto.user;

import lombok.Builder;
import ru.mudan.dto.user.enums.RegistrationStatus;

@Builder
public record RegistrationResponse(
    RegistrationStatus status,
    Long registrationId
) {
}
