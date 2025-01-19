package ru.mudan.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.mudan.dto.user.event.UserCreatingEvent;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserCreatingEvent(UserCreatingEvent event) {
        kafkaTemplate.send("user-creating", event);
    }
}
