package ru.mudan.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.mudan.dto.user.auth.RegisterRequest;
import ru.mudan.dto.user.event.UserCreatingEvent;
import ru.mudan.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final UserService userService;

    @KafkaListener(topics = "user-creating", groupId = "user-creating_consumer")
    public void listenUserCreating(UserCreatingEvent event) {
        userService.save(RegisterRequest.builder()
                        .email(event.email())
                        .firstname(event.firstname())
                        .lastname(event.lastname())
                .build(), event.registrationId());
    }
}
