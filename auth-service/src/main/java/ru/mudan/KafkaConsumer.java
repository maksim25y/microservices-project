package ru.mudan;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    @KafkaListener(topics = "message-topic", groupId = "my_consumer")
    public void listen(Object o) {
        log.info("Message received: {}", o.toString());
    }
}
