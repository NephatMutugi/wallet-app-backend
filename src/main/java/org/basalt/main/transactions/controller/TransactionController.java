package org.basalt.main.transactions.controller;

import org.basalt.main.common.payloads.LoggingParameter;
import org.basalt.main.common.payloads.ResponsePayload;
import org.basalt.main.common.utils.CommonUtils;
import org.basalt.main.transactions.model.Transaction;
import org.basalt.main.transactions.service.TransactionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 21/04/2024
 */
@RestController
@RequestMapping("api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final CommonUtils commonUtils;

    public TransactionController(TransactionService transactionService, CommonUtils commonUtils) {
        this.transactionService = transactionService;
        this.commonUtils = commonUtils;
    }

    @GetMapping("/by-type")
    public ResponseEntity<ResponsePayload<List<Transaction>>> getTransactionsByType(
            @RequestParam String transactionType,
            @RequestHeader HttpHeaders headers) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return transactionService.findTransactionsByType(transactionType, loggingParameter);
    }

    @GetMapping("/by-wallet-id")
    public ResponseEntity<ResponsePayload<List<Transaction>>> getTransactionsByWalletId(
            @RequestParam UUID walletId,
            @RequestHeader HttpHeaders headers) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return transactionService.findTransactionsByWalletId(walletId, loggingParameter);
    }

    @GetMapping("/by-date")
    public ResponseEntity<ResponsePayload<List<Transaction>>> getTransactionsByTransactionDate(
            @RequestParam Date transactionDate,
            @RequestHeader HttpHeaders headers) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return transactionService.findTransactionsByTransactionDate(transactionDate, loggingParameter);
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<ResponsePayload<List<Transaction>>> getTransactionsByDateRange(
            @RequestParam Date startDate,
            @RequestParam Date endDate,
            @RequestHeader HttpHeaders headers) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return transactionService.findTransactionsByDateRange(startDate, endDate, loggingParameter);
    }
}
