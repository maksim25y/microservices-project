package ru.mudan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mudan.dto.user.UserResponse;
import ru.mudan.dto.user.UserUpdateRequest;
import ru.mudan.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Пользователи", description = "API управления пользователями")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
public class UserController {

    private final UserService userService;

    @GetMapping("/{email}")
    public UserResponse getUserById(@PathVariable String email) {
        return userService.getByEmail(email);
    }

    @Operation(summary = "Обновление информации о пользователе",
            description = "Обновляет информацию о пользователе с предоставленными данными")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное обновление",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверный ввод",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PatchMapping
    public UserResponse updateUser(@RequestBody UserUpdateRequest request, Authentication authentication) {
        return userService.update(authentication, request);
    }

    @Operation(summary = "Удаление информации о пользователе",
            description = "Удаляет информацию о пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешное удаление",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Ошибка аутентификации",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        userService.delete(authentication);
        return ResponseEntity.noContent().build();
    }
}
