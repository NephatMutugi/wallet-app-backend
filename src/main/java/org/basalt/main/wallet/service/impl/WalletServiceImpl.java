package org.basalt.main.wallet.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.basalt.main.common.exceptions.ApplicationException;
import org.basalt.main.common.payloads.ApiResponse;
import org.basalt.main.common.payloads.LoggingParameter;
import org.basalt.main.common.payloads.ResponsePayload;
import org.basalt.main.common.utils.StatusCode;
import org.basalt.main.common.utils.StatusMessage;
import org.basalt.main.customer.model.CurrentUserSession;
import org.basalt.main.customer.model.Customer;
import org.basalt.main.customer.repository.CurrentSessionRepo;
import org.basalt.main.customer.repository.CustomerRepo;
import org.basalt.main.transactions.model.Transaction;
import org.basalt.main.wallet.model.Wallet;
import org.basalt.main.wallet.model.dto.FundsTransferRequest;
import org.basalt.main.wallet.model.dto.FundsTransferResponse;
import org.basalt.main.wallet.model.dto.WalletDto;
import org.basalt.main.wallet.repository.WalletRepo;
import org.basalt.main.wallet.service.KafkaProducer;
import org.basalt.main.wallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.basalt.main.common.utils.CommonUtils.getJsonString;
import static org.basalt.main.common.utils.StatusCode.BAD_REQUEST;
import static org.basalt.main.common.utils.StatusMessage.*;

/**
 * @ Author Nephat Muchiri
 * Date 18/04/2024
 */
@Service
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final WalletRepo walletRepo;
    private final CustomerRepo customerRepo;
    private final CurrentSessionRepo currentSessionRepo;
    private final KafkaProducer kafkaProducer;

    public WalletServiceImpl(WalletRepo walletRepo, CustomerRepo customerRepo, CurrentSessionRepo currentSessionRepo, KafkaProducer kafkaProducer) {
        this.walletRepo = walletRepo;
        this.customerRepo = customerRepo;
        this.currentSessionRepo = currentSessionRepo;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public ResponseEntity<ResponsePayload<WalletDto>> showBalance(LoggingParameter loggingParameter, String mobile, String key) {
        // Retrieve the current user's session using the key
        CurrentUserSession currUserSession = currentSessionRepo.findByToken(key);
        if(currUserSession==null) {
            throw new ApplicationException(StatusCode.UNAUTHORIZED, loggingParameter.getRequestId(), StatusMessage.NO_LOGGED_IN_CUSTOMER, StatusMessage.NO_LOGGED_IN_CUSTOMER);
        }

        Customer customer = customerRepo.findCustomerByMobileNumber(mobile);
        if (customer == null){
            throw new RuntimeException("No customer linked to the mobile number");
        }
        Wallet wallet = walletRepo.findWalletByCustomerId(customer.getCustomerId());
        if (wallet == null){
            throw new RuntimeException("Wallet not found");
        }
        WalletDto response = WalletDto.builder()
                .customer(customer)
                .walletId(wallet.getWalletId())
                .balance(wallet.getBalance())
                .build();

        ApiResponse.Header header = new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.OK, StatusMessage.SUCCESS, StatusMessage.SUCCESS, LocalDateTime.now().toString());
        return ResponseEntity.ok(new ResponsePayload<>(header, response));
    }

    @Transactional
    @Override
    public ResponseEntity<ResponsePayload<FundsTransferResponse>> fundTransfer(LoggingParameter loggingParameter, String key, FundsTransferRequest request) {
        // Retrieve the current user's session using the key
        CurrentUserSession currUserSession = currentSessionRepo.findByToken(key);
        if(currUserSession==null) {
            throw new ApplicationException(StatusCode.UNAUTHORIZED, loggingParameter.getRequestId(), StatusMessage.NO_LOGGED_IN_CUSTOMER, StatusMessage.NO_LOGGED_IN_CUSTOMER);
        }
        if (request.getTransactionType().equalsIgnoreCase("DEBIT")) {
            return handleDebitTransaction(loggingParameter, request);
        } else if (request.getTransactionType().equalsIgnoreCase("CREDIT")) {
            return handleCreditTransaction(loggingParameter, request);
        } else {
            throw new ApplicationException(BAD_REQUEST, loggingParameter.getRequestId(), INVALID_TRANSACTION_TYPE, INVALID_TRANSACTION_TYPE);
        }
    }

    private ResponseEntity<ResponsePayload<FundsTransferResponse>> handleDebitTransaction(LoggingParameter loggingParameter, FundsTransferRequest request) {
        Wallet fromWallet = walletRepo.findWalletByCustomerMobile(request.getFrom());
        Wallet toWallet = walletRepo.findWalletByCustomerMobile(request.getTo());

        if (fromWallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new ApplicationException(StatusCode.BAD_REQUEST, loggingParameter.getRequestId(), INSUFFICIENT_FUNDS, INSUFFICIENT_FUNDS);
        }
        log.info("BEFORE: {}", getJsonString(fromWallet));
        fromWallet.setBalance(fromWallet.getBalance().subtract(request.getAmount()));
        log.info("AFTER: {}", getJsonString(fromWallet));
        toWallet.setBalance(toWallet.getBalance().add(request.getAmount()));

        walletRepo.saveAll(List.of(fromWallet, toWallet));

        // Send transaction to KAFKA for saving
        Transaction transaction = Transaction.builder()
                .transactionId(UUID.fromString(loggingParameter.getRequestId()))
                .transactionType("DEBIT")
                .transactionDate(Date.from(Instant.now()))
                .walletId(fromWallet.getWalletId())
                .Description("Debit From: " + fromWallet.getWalletId() + ". Credit To: " + toWallet.getWalletId())
                .amount(request.getAmount())
                .build();
        kafkaProducer.sendTransaction("transactions_topic", transaction);
        return buildFundsTransferResponse(loggingParameter, fromWallet, request.getAmount());
    }

    private ResponseEntity<ResponsePayload<FundsTransferResponse>> handleCreditTransaction(LoggingParameter loggingParameter, FundsTransferRequest request) {
        try {
            Wallet wallet = walletRepo.findWalletByCustomerMobile(request.getTo());

            if (request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                throw new ApplicationException(BAD_REQUEST, loggingParameter.getRequestId(), NEGATIVE_CREDIT, NEGATIVE_CREDIT);
            }

            wallet.setBalance(wallet.getBalance().add(request.getAmount()));
            walletRepo.save(wallet);

            Transaction transaction = Transaction.builder()
                    .transactionId(UUID.fromString(loggingParameter.getRequestId()))
                    .transactionType("CREDIT")
                    .transactionDate(Date.from(Instant.now()))
                    .walletId(wallet.getWalletId())
                    .Description("Credit To: " + wallet.getWalletId())
                    .amount(request.getAmount())
                    .build();
            kafkaProducer.sendTransaction("transactions_topic", transaction);
            return buildFundsTransferResponse(loggingParameter, wallet, request.getAmount());
        } catch (Exception e){
            throw new RuntimeException("Error while adding funds");
        }
    }

    private ResponseEntity<ResponsePayload<FundsTransferResponse>> buildFundsTransferResponse(LoggingParameter loggingParameter, Wallet wallet, BigDecimal amount) {
        FundsTransferResponse response = FundsTransferResponse.builder()
                .transactionAmount(amount)
                .body(wallet)
                .build();

        ApiResponse.Header header = new ApiResponse.Header(
                loggingParameter.getRequestId(),
                StatusCode.OK,
                StatusMessage.SUCCESS,
                "Transaction completed successfully",
                LocalDateTime.now().toString()
        );

        return ResponseEntity.ok(new ResponsePayload<>(header, response));
    }
}
