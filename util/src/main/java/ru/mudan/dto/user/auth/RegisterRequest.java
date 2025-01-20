package ru.mudan.dto.user.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "Запрос на регистрацию, содержащий информацию о новом пользователе")
public record RegisterRequest(
        @Schema(description = "Имя пользователя", example = "Иван")
        @NotBlank(message = "{user.firstname.is_blank}")
        @Size(min = 2, max = 32, message = "{user.firstname.invalid_size}")
        @JsonProperty("firstname")
        String firstname,
        @Schema(description = "Фамилия пользователя", example = "Иванов")
        @NotBlank(message = "{user.lastname.is_blank}")
        @Size(min = 2, max = 32, message = "{user.lastname.invalid_size}")
        @JsonProperty("lastname")
        String lastname,
        @Schema(description = "Адрес электронной почты пользователя", example = "test@mail.ru")
        @Pattern(message = "{user.email.invalid}",
                regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        @NotBlank(message = "{user.email.is_blank}")
        String email,
        @Schema(description = "Пароль пользователя", example = "password")
        @Size(min = 8, max = 32, message = "{user.password.invalid_size}")
        @NotBlank(message = "{user.password.is_blank}")
        String password
) {
}
