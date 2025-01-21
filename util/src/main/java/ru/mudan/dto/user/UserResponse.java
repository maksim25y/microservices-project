package ru.mudan.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Ответ на запрос, содержащий информацию о пользователе")
public record UserResponse(
        @Schema(description = "Id пользователя", example = "1")
        @JsonProperty("id")
        Long id,
        @Schema(description = "Имя пользователя", example = "Иван")
        @JsonProperty("firstname")
        String firstname,
        @Schema(description = "Фамилия пользователя", example = "Иванов")
        @JsonProperty("lastname")
        String lastname,
        @JsonProperty("email")
        @Schema(description = "Адрес электронной почты пользователя", example = "test@mail.ru")
        String email
) {
}
