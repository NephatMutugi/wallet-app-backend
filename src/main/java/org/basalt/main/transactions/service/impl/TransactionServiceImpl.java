package org.basalt.main.transactions.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.basalt.main.common.payloads.ApiResponse;
import org.basalt.main.common.payloads.LoggingParameter;
import org.basalt.main.common.payloads.ResponsePayload;
import org.basalt.main.common.utils.StatusCode;
import org.basalt.main.transactions.model.Transaction;
import org.basalt.main.transactions.repository.TransactionsRepo;
import org.basalt.main.transactions.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 21/04/2024
 */
@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionsRepo transactionsRepo;

    public TransactionServiceImpl(TransactionsRepo transactionsRepo) {
        this.transactionsRepo = transactionsRepo;
    }

    @Override
    public ResponseEntity<ResponsePayload<List<Transaction>>> findTransactionsByType(String transactionType, LoggingParameter loggingParameter) {
        try {
            log.info("Fetching transactions by type: {}", transactionType);
            List<Transaction> transactions = transactionsRepo.findByTransactionType(transactionType);
            if (transactions.isEmpty()) {
                log.info("No transactions found for type: {}", transactionType);
                return ResponseEntity.ok(new ResponsePayload<>(new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.NOT_FOUND, "No transactions found", "No transactions found", LocalDateTime.now().toString()), null));
            }
            return ResponseEntity.ok(new ResponsePayload<>(new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.OK, "Success", "Transactions found", LocalDateTime.now().toString()), transactions));
        } catch (Exception e) {
            log.error("Error fetching transactions by type: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(new ResponsePayload<>(new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.INTERNAL_SERVER_ERROR, "Internal Server Error", "Failed to retrieve transactions", LocalDateTime.now().toString()), null));
        }
    }

    @Override
    public ResponseEntity<ResponsePayload<List<Transaction>>> findTransactionsByWalletId(UUID walletId, LoggingParameter loggingParameter) {
        try {
            log.info("Fetching transactions by wallet ID: {}", walletId);
            List<Transaction> transactions = transactionsRepo.findTransactionByWalletId(walletId);
            if (transactions.isEmpty()) {
                log.info("No transactions found linked to wallet ID: {}", walletId);
                return ResponseEntity.ok(new ResponsePayload<>(new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.NOT_FOUND, "No transactions found", "No transactions linked to wallet", LocalDateTime.now().toString()), null));
            }
            return ResponseEntity.ok(new ResponsePayload<>(new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.OK, "Success", "Transactions found", LocalDateTime.now().toString()), transactions));
        } catch (Exception e) {
            log.error("Error fetching transactions by wallet ID: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(new ResponsePayload<>(new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.INTERNAL_SERVER_ERROR, "Internal Server Error", "Failed to retrieve transactions", LocalDateTime.now().toString()), null));
        }
    }

    @Override
    public ResponseEntity<ResponsePayload<List<Transaction>>> findTransactionsByTransactionDate(Date transactionDate, LoggingParameter loggingParameter) {
        try {
            log.info("Fetching transactions by date: {}", transactionDate);
            List<Transaction> transactions = transactionsRepo.findTransactionByTransactionDate(transactionDate);
            if (transactions.isEmpty()) {
                log.info("No transactions found on date: {}", transactionDate);
                return ResponseEntity.ok(new ResponsePayload<>(new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.NOT_FOUND, "No transactions found", "No transactions on this date", LocalDateTime.now().toString()), null));
            }
            return ResponseEntity.ok(new ResponsePayload<>(new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.OK, "Success", "Transactions found", LocalDateTime.now().toString()), transactions));
        } catch (Exception e) {
            log.error("Error fetching transactions by transaction date: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(new ResponsePayload<>(new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.INTERNAL_SERVER_ERROR, "Internal Server Error", "Failed to retrieve transactions", LocalDateTime.now().toString()), null));
        }
    }

    @Override
    public ResponseEntity<ResponsePayload<List<Transaction>>> findTransactionsByDateRange(Date startDate, Date endDate, LoggingParameter loggingParameter) {
        try {
            log.info("Fetching transactions between dates: {} and {}", startDate, endDate);
            List<Transaction> transactions = transactionsRepo.findByTransactionDateBetween(startDate, endDate);
            if (transactions.isEmpty()) {
                log.info("No transactions found within date range: {} to {}", startDate, endDate);
                return ResponseEntity.ok(new ResponsePayload<>(new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.NOT_FOUND, "No transactions found", "No transactions within date range", LocalDateTime.now().toString()), null));
            }
            return ResponseEntity.ok(new ResponsePayload<>(new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.OK, "Success", "Transactions found", LocalDateTime.now().toString()), transactions));
        } catch (Exception e) {
            log.error("Error fetching transactions by date range: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(new ResponsePayload<>(new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.INTERNAL_SERVER_ERROR, "Internal Server Error", "Failed to retrieve transactions", LocalDateTime.now().toString()), null));
        }
    }
}
