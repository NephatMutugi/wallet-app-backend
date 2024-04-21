package org.basalt.main.transactions.service;

import org.basalt.main.common.payloads.LoggingParameter;
import org.basalt.main.common.payloads.ResponsePayload;
import org.basalt.main.transactions.model.Transaction;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 21/04/2024
 */
public interface TransactionService {
    ResponseEntity<ResponsePayload<List<Transaction>>> findTransactionsByType(String transactionType, LoggingParameter loggingParameter);
    ResponseEntity<ResponsePayload<List<Transaction>>> findTransactionsByWalletId(UUID walletId, LoggingParameter loggingParameter);
    ResponseEntity<ResponsePayload<List<Transaction>>> findTransactionsByTransactionDate(Date transactionDate, LoggingParameter loggingParameter);
    ResponseEntity<ResponsePayload<List<Transaction>>> findTransactionsByDateRange(Date startDate, Date endDate, LoggingParameter loggingParameter);
}
