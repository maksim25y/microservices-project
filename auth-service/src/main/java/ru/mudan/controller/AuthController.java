package ru.mudan.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mudan.dto.user.auth.RegisterRequest;
import ru.mudan.dto.user.auth.RegistrationResponse;
import ru.mudan.dto.user.auth.AuthRequest;
import ru.mudan.dto.user.auth.TokenResponse;
import ru.mudan.service.AuthService;
import static ru.mudan.dto.user.enums.RegisterUtil.statuses;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }

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
