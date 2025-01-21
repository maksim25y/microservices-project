package ru.mudan.dto.user.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "Запрос на обновление токена")
public record RefreshTokenRequest(
        @Schema(description = "Refresh токен", example = "d33fcba4-b8cf-44db-8641-b299421b3c26")
        @NotBlank(message = "{auth.refresh_token.is_blank}")
        @JsonProperty("refresh_token")
        String refreshToken
) {
}
