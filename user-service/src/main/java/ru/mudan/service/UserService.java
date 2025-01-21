package ru.mudan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.mudan.dto.user.UserResponse;
import ru.mudan.dto.user.UserUpdateRequest;
import ru.mudan.dto.user.auth.RegisterRequest;
import ru.mudan.dto.user.event.UserCreatedEvent;
import ru.mudan.dto.user.event.UserNotCreatedEvent;
import ru.mudan.dto.user.event.UserUpdatingEvent;
import ru.mudan.entity.AppUser;
import ru.mudan.exceptions.entity.not_found.UserNotFoundException;
import ru.mudan.kafka.KafkaProducer;
import ru.mudan.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KafkaProducer kafkaProducer;
    private final UserRepository userRepository;

    public UserResponse getByEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return UserResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .build();
    }

    public void save(RegisterRequest request) {
        var user = new AppUser();
        user.setFirstname(request.firstname());
        user.setLastname(request.lastname());
        user.setEmail(request.email());

        try {
            var savedUser = userRepository.save(user);
        } catch (Exception e) {
            var userNotCreatedEvent = new UserNotCreatedEvent(
                    user.getFirstname(),
                    user.getLastname(),
                    user.getEmail()
            );
            kafkaProducer.sendUserNotCreatedEvent(userNotCreatedEvent);
        }

        var userCreatedEvent = new UserCreatedEvent(
                user.getFirstname(),
                user.getLastname(),
                user.getEmail()
        );

        kafkaProducer.sendUserCreatedEvent(userCreatedEvent);
    }

    public UserResponse update(Authentication authentication, UserUpdateRequest request) {
        var user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException(authentication.getName()));
        user.setFirstname(request.firstname());
        user.setLastname(request.lastname());
        var updatedUser = userRepository.save(user);

        var userUpdatingEvent = UserUpdatingEvent.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(user.getEmail())
                .build();

        kafkaProducer.sendUserUpdating(userUpdatingEvent);

        return UserResponse.builder()
                .id(updatedUser.getId())
                .firstname(updatedUser.getFirstname())
                .lastname(updatedUser.getLastname())
                .email(updatedUser.getEmail())
                .build();
    }

    public void delete(Authentication authentication) {
        var user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException(authentication.getName()));
        userRepository.delete(user);
        kafkaProducer.sendUserDeleting(user.getEmail());
    }
}
