package org.basalt.main.transactions.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.basalt.main.transactions.model.Transaction;
import org.basalt.main.transactions.repository.TransactionsRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @ Author Nephat Muchiri
 * Date 16/04/2024
 */
@Service
@Slf4j
public class KafkaConsumer {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    private final TransactionsRepo transactionsRepo;

    public KafkaConsumer(TransactionsRepo transactionsRepo) {
        this.transactionsRepo = transactionsRepo;
    }


    @KafkaListener(topics = "transactions_topic", groupId = "transactions-group")
    public void listenTransaction(String message) {
        try {
            Transaction transaction = new ObjectMapper().readValue(message, Transaction.class);
            // Process the transaction, e.g., log it, or integrate further business logic
            System.out.println("Received Transaction: " + transaction);
            transactionsRepo.save(transaction);
        } catch (IOException e) {
            System.err.println("Failed to deserialize transaction: " + e.getMessage());
        }
    }





}
