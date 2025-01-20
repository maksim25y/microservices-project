package ru.mudan.dto.user.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "Запрос на вход в аккаунт")
public record AuthRequest(
        @JsonProperty("email")
        @NotBlank(message = "${user.email.is_blank}")
        @Schema(description = "Адрес электронной почты пользователя", example = "test@mail.ru")
        String email,
        @JsonProperty("password")
        @NotBlank(message = "${user.password.is_blank}")
        @Schema(description = "Пароль", example = "password")
        String password) {
}
