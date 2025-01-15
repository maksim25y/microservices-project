package ru.mudan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.mudan.dto.user.UserResponse;
import ru.mudan.dto.user.UserUpdateRequest;
import ru.mudan.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(); //TODO - добавить исключение
        return UserResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .build();
    }

    public UserResponse update(Authentication authentication, UserUpdateRequest request) {
        var user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(); //TODO - добавить исключение
        user.setFirstname(request.firstname());
        user.setLastname(request.lastname());
        var updatedUser = userRepository.save(user);
        return UserResponse.builder()
                .id(updatedUser.getId())
                .firstname(updatedUser.getFirstname())
                .lastname(updatedUser.getLastname())
                .email(updatedUser.getEmail())
                .build();
    }

    public void delete(Authentication authentication) {
        var user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(); //TODO - добавить исключение
        userRepository.delete(user);
    }
}
