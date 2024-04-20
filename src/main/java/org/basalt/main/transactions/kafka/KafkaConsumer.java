package org.basalt.main.transactions.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.UUID;
import static org.basalt.main.common.utils.StringConstants.LOG_FORMATTER;

/**
 * @ Author Nephat Muchiri
 * Date 16/04/2024
 */
@Service
@Slf4j
public class KafkaConsumer {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;




    @KafkaListener(topics = "transactions_topic", groupId = "wallet-group")
    public void processMessage(String message){

        log.info("REQUEST PROCESSOR LISTENING...");

        TransactionDto transactionDto = convertJsonToTransactionDTO(message.trim());
        log.info(LOG_FORMATTER, transactionDto.getTransactionId(), "DEBIT", "PROCESS QUEUED MESSAGE");



        UUID accountId = transactionDto.getAccountId();
        UUID currentLock = transactionDto.getLockId();
        BigDecimal debitAmount = transactionDto.getAmount();
        Account account = accountRepository.findAccountById(transactionDto.getAccountId());
        BigDecimal lockedFunds = lockedFundsRepository.getTotalLockedAmountExcludingCurrentLock(accountId, currentLock);
        if (lockedFunds == null){
            lockedFunds = BigDecimal.ZERO;
        }
        log.info("LOCKED FUNDS: {}", lockedFunds);
        BigDecimal availableBalance = account.getBalance().subtract(lockedFunds);
        if (availableBalance.compareTo(debitAmount)<0){
            log.info("INSUFFICIENT FUNDS");
            deleteLock(transactionDto.getLockId());
            emailService.sendEmail(transactionDto.getUserEmail(), "DEBIT", "Insufficient funds", transactionDto.getType());
            return;
        }
        log.info("BEFORE: {}", account.getBalance());

        account.setBalance(account.getBalance().subtract(debitAmount));
        log.info("SAVE: {}", account.getBalance());
        accountRepository.save(account);


        log.info("ACCOUNT: {}", account);
        debitAccount(transactionDto);
        // Delete lock after debit
        deleteLock(transactionDto.getLockId());
        // Send email notification
        String email = transactionDto.getUserEmail();
        String subject = "BASALT BANK ACCOUNT DEBIT";
        String body = "A debit of " + transactionDto.getAmount() + " has been made for account: " + transactionDto.getAccountNumber();
        emailService.sendEmail(email, subject, body, transactionDto.getType());
    }



}
