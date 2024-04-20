package org.basalt.main.transactions.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @ Author Nephat Muchiri
 * Date 16/04/2024
 */
@Service
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendDebitRequest(String topic, String message){
        kafkaTemplate.send(topic, message);
    }
}
