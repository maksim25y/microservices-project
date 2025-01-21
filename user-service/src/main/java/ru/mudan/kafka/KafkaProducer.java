package ru.mudan.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.mudan.dto.user.event.UserCreatedEvent;
import ru.mudan.dto.user.event.UserNotCreatedEvent;
import ru.mudan.dto.user.event.UserUpdatingEvent;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserCreatedEvent(UserCreatedEvent event) {
        kafkaTemplate.send("user-created", event);
    }

    public void sendUserNotCreatedEvent(UserNotCreatedEvent event) {
        kafkaTemplate.send("user-not-created", event);
    }

    public void sendUserDeleting(String email) {
        kafkaTemplate.send("user-deleting", email);
    }

    public void sendUserUpdating(UserUpdatingEvent event) {
        kafkaTemplate.send("user-updating", event);
    }
}
