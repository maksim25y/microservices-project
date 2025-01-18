package ru.mudan;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.mudan.dto.user.event.UserCreatedEvent;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(UserCreatedEvent event) {
        kafkaTemplate.send("message-topic", event);
    }
}