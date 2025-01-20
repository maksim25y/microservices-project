package ru.mudan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mudan.dto.user.auth.AuthRequest;
import ru.mudan.dto.user.auth.RegisterRequest;
import ru.mudan.dto.user.auth.RegistrationResponse;
import ru.mudan.dto.user.auth.TokenResponse;
import ru.mudan.service.AuthService;
import static ru.mudan.dto.user.enums.RegisterUtil.statuses;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Аутентификация", description = "API управления аутентификацией")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Аутентификация пользователя",
            description = "Аутентифицирует пользователя с предоставленными учетными данными")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная аутентификация",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверный ввод",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    @Operation(summary = "Регистрация нового пользователя",
            description = "Регистрирует нового пользователя с предоставленной информацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная регистрация"),
            @ApiResponse(responseCode = "400", description = "Неверный ввод",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Пользователь уже существует",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @SecurityRequirement(name = "JWT")
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        var registrationResponse = authService.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(registrationResponse);
    }

    @GetMapping("/registration-status/{registrationId}")
    public ResponseEntity<RegistrationResponse> getRegistrationStatus(
            @PathVariable("registrationId") Long registrationId) {
        var registrationResponse = authService.getRegistrationResponseById(registrationId);
        return ResponseEntity.status(statuses.get(registrationResponse.status()))
                .body(registrationResponse);
    }
}
