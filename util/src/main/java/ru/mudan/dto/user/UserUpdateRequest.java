package ru.mudan.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "Запрос на обновление информации о пользователе")
public record UserUpdateRequest(
        @Schema(description = "Имя пользователя", example = "Иван")
        @NotBlank(message = "{user.firstname.is_blank}")
        @Size(min = 2, max = 32, message = "{user.firstname.invalid_size}")
        @JsonProperty("firstname")
        String firstname,
        @Schema(description = "Фамилия пользователя", example = "Иванов")
        @NotBlank(message = "{user.lastname.is_blank}")
        @Size(min = 2, max = 32, message = "{user.lastname.invalid_size}")
        @JsonProperty("lastname")
        String lastname
) {
}
