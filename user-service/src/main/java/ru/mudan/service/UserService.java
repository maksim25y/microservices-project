package ru.mudan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.mudan.KafkaProducer;
import ru.mudan.dto.user.RegisterRequest;
import ru.mudan.dto.user.UserResponse;
import ru.mudan.dto.user.UserUpdateRequest;
import ru.mudan.dto.user.event.UserCreatedEvent;
import ru.mudan.entity.AppUser;
import ru.mudan.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KafkaProducer kafkaProducer;
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

    public UserResponse save(RegisterRequest request) {
        var user = new AppUser();
        user.setFirstname(request.firstname());
        user.setLastname(request.lastname());
        user.setEmail(request.email());
        var savedUser = userRepository.save(user);

        var userCreatedEvent = new UserCreatedEvent(
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail()
        );

        kafkaProducer.sendMessage(userCreatedEvent);

        return UserResponse.builder()
                .id(savedUser.getId())
                .firstname(savedUser.getFirstname())
                .lastname(savedUser.getLastname())
                .email(savedUser.getEmail())
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
