package ru.mudan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.mudan.dto.user.RegisterRequest;
import ru.mudan.dto.user.UserResponse;
import ru.mudan.dto.user.UserUpdateRequest;
import ru.mudan.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PatchMapping("/update")
    public UserResponse updateUser(@RequestBody UserUpdateRequest request, Authentication authentication) {
        return userService.update(authentication, request);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        userService.delete(authentication);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add")
    public UserResponse createUser(@RequestBody RegisterRequest request) {
        return userService.save(request);
    }
}
