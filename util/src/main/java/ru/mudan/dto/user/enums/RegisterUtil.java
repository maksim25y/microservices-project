package ru.mudan.dto.user.enums;

import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings("MagicNumber")
public class RegisterUtil {
    public static Map<RegistrationStatus, Integer> statuses = Map.of(
            RegistrationStatus.COMPLETED, 201,
            RegistrationStatus.PENDING, 202,
            RegistrationStatus.FAILED, 400
    );
}
