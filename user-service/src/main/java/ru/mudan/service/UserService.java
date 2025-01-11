package ru.mudan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mudan.dto.user.UserResponse;
import ru.mudan.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getById(Long id) {
        var user = userRepository.findById(id).orElseThrow();
        return UserResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .build();
    }
}
