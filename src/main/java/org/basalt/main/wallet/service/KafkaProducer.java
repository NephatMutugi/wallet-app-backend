package org.basalt.main.wallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.basalt.main.transactions.model.Transaction;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @ Author Nephat Muchiri
 * Date 16/04/2024
 */
@Service
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendTransaction(String topic, Transaction transaction){
        try{
            String message = objectMapper.writeValueAsString(transaction);
            kafkaTemplate.send(topic, message);
        } catch (JsonProcessingException e){
            log.error("kafka-producer: {}", e.getMessage());
            throw new RuntimeException("Failed to serialize transaction");
        }
    }

}
