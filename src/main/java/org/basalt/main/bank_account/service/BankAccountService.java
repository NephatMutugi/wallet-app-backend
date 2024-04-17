package org.basalt.main.bank_account.service;

import org.basalt.main.bank_account.model.BankAccount;
import org.basalt.main.bank_account.model.dto.BankAccountDTO;
import org.basalt.main.common.payloads.LoggingParameter;
import org.basalt.main.common.payloads.ResponsePayload;
import org.basalt.main.wallet.model.Wallet;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
public interface BankAccountService {
    ResponseEntity<ResponsePayload<BankAccount>> addBankAccount(LoggingParameter loggingParameter, String key, BankAccountDTO bankAccountDTO);

    ResponseEntity<ResponsePayload<Wallet>> removeBankAccount(LoggingParameter loggingParameter, String key,BankAccountDTO bankAccountDTO);

    ResponseEntity<ResponsePayload<BankAccount>> viewBankAccount(LoggingParameter loggingParameter, String key, long accountNo);

     ResponseEntity<ResponsePayload<List<BankAccount>>> viewAllBankAccounts(LoggingParameter loggingParameter, String key);
}
