package ru.mudan.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.mudan.dto.user.enums.RegistrationStatus;
import ru.mudan.dto.user.event.UserCreatedEvent;
import ru.mudan.dto.user.event.UserNotCreatedEvent;
import ru.mudan.exceptions.entity.not_found.RegistrationNotFoundException;
import ru.mudan.repositories.RegistrationRepository;
import ru.mudan.service.AuthService;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final RegistrationRepository registrationRepository;
    private final AuthService authService;

    @KafkaListener(topics = "user-created", groupId = "user-created_consumer")
    public void listenUserCreated(UserCreatedEvent event) {
        var registration = registrationRepository.findById(event.registrationId())
                .orElseThrow(() -> new RegistrationNotFoundException(event.registrationId()));
        registration.setStatus(RegistrationStatus.COMPLETED);
        registrationRepository.save(registration);
        //TODO - отправка нотификации на почту, но это уже NotificationService
        log.info("User created with email: {}", event.email());
    }

    @KafkaListener(topics = "user-not-created", groupId = "user-not-created_consumer")
    public void listenUserNotCreated(UserNotCreatedEvent event) {
        var registration = registrationRepository.findById(event.registrationId())
                .orElseThrow(() -> new RegistrationNotFoundException(event.registrationId()));
        registration.setStatus(RegistrationStatus.FAILED);
        registrationRepository.save(registration);
        authService.delete(event.email());
        //TODO - отправка нотификации на почту, но это уже NotificationService
        log.info("User not created with email: {}", event.email());
    }

    @KafkaListener(topics = "user-deleting", groupId = "user-deleting_consumer")
    public void listenUserDeleting(String email) {
        authService.delete(email);
    }
}
