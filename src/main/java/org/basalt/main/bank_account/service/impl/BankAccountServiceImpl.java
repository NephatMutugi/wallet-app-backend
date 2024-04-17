package org.basalt.main.bank_account.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.basalt.main.bank_account.model.BankAccount;
import org.basalt.main.bank_account.model.dto.BankAccountDTO;
import org.basalt.main.bank_account.repository.BankAccountRepo;
import org.basalt.main.bank_account.service.BankAccountService;
import org.basalt.main.common.exceptions.ApplicationException;
import org.basalt.main.common.payloads.ApiResponse;
import org.basalt.main.common.payloads.LoggingParameter;
import org.basalt.main.common.payloads.ResponsePayload;
import org.basalt.main.common.utils.StatusCode;
import org.basalt.main.common.utils.StatusMessage;
import org.basalt.main.customer.model.CurrentUserSession;
import org.basalt.main.customer.repository.CurrentSessionRepo;
import org.basalt.main.wallet.model.Wallet;
import org.basalt.main.wallet.repository.WalletRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
@Service
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepo bankAccountRepo;
    private final CurrentSessionRepo currentSessionRepo;
    private final WalletRepo walletRepo;

    // Constructor to inject repository dependencies
    public BankAccountServiceImpl(BankAccountRepo bankAccountRepo, CurrentSessionRepo currentSessionRepo, WalletRepo walletRepo) {
        this.bankAccountRepo = bankAccountRepo;
        this.currentSessionRepo = currentSessionRepo;
        this.walletRepo = walletRepo;
    }

    // Adds a bank account to the system
    @Override
    public ResponseEntity<ResponsePayload<BankAccount>> addBankAccount(LoggingParameter loggingParameter,  String key, BankAccountDTO request) {
        // Retrieve the current user's session using the key
        CurrentUserSession currUserSession = currentSessionRepo.findByToken(key);
        if(currUserSession==null) {
            throw new ApplicationException(StatusCode.UNAUTHORIZED, loggingParameter.getRequestId(), StatusMessage.NO_LOGGED_IN_CUSTOMER, StatusMessage.NO_LOGGED_IN_CUSTOMER);
        }

        BankAccount bankAccount = bankAccountRepo.findByAccountNo(request.getAccountNo());
        if (!(bankAccount == null)){
            throw new ApplicationException(StatusCode.CONFLICT, loggingParameter.getRequestId(), StatusMessage.DUPLICATE_BANK, StatusMessage.DUPLICATE_BANK);
        }

        Wallet wallet =  walletRepo.showCustomerWalletDetails(currUserSession.getUserId());
        BankAccount createBankAccount = new BankAccount(request.getAccountNo(), request.getBankCode(), request.getBankName(), request.getBalance());
        createBankAccount.setWallet(wallet);
        createBankAccount = bankAccountRepo.save(createBankAccount);
        ApiResponse.Header header = new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.OK, StatusMessage.SUCCESS, StatusMessage.SUCCESS, LocalDateTime.now().toString());
        return ResponseEntity.ok(new ResponsePayload<>(header, createBankAccount));
    }

    // Removes a bank account from the system
    @Override
    public ResponseEntity<ResponsePayload<Wallet>> removeBankAccount(LoggingParameter loggingParameter, String key, BankAccountDTO request) {
        // Retrieve the current user's session using the key
        CurrentUserSession currUserSession = currentSessionRepo.findByToken(key);
        if(currUserSession==null) {
            throw new ApplicationException(StatusCode.UNAUTHORIZED, loggingParameter.getRequestId(), StatusMessage.NO_LOGGED_IN_CUSTOMER, StatusMessage.NO_LOGGED_IN_CUSTOMER);
        }

        BankAccount bankAccount = bankAccountRepo.findByAccountNo(request.getAccountNo());
        if (bankAccount == null){
            throw new ApplicationException(StatusCode.BAD_REQUEST, loggingParameter.getRequestId(), StatusMessage.BANK_NOT_FOUND, StatusMessage.BANK_NOT_FOUND);
        }
        Wallet wallet = bankAccount.getWallet();
        bankAccountRepo.delete(bankAccount);
        ApiResponse.Header header = new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.OK, StatusMessage.SUCCESS, StatusMessage.SUCCESS, LocalDateTime.now().toString());
        return ResponseEntity.ok(new ResponsePayload<>(header, wallet));
    }

    // Retrieves a single bank account details
    @Override
    public ResponseEntity<ResponsePayload<BankAccount>> viewBankAccount(LoggingParameter loggingParameter, String key, long accountNo) {
        // Retrieve the current user's session using the key
        CurrentUserSession currUserSession = currentSessionRepo.findByToken(key);
        if(currUserSession==null) {
            throw new ApplicationException(StatusCode.UNAUTHORIZED, loggingParameter.getRequestId(), StatusMessage.NO_LOGGED_IN_CUSTOMER, StatusMessage.NO_LOGGED_IN_CUSTOMER);
        }
        BankAccount bankAccount = bankAccountRepo.findByAccountNo(accountNo);
        if (bankAccount == null){
            throw new ApplicationException(StatusCode.BAD_REQUEST, loggingParameter.getRequestId(), StatusMessage.BANK_NOT_FOUND, StatusMessage.BANK_NOT_FOUND);
        }

        ApiResponse.Header header = new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.OK, StatusMessage.SUCCESS, StatusMessage.SUCCESS, LocalDateTime.now().toString());
        return ResponseEntity.ok(new ResponsePayload<>(header, bankAccount));
    }

    // Retrieves all bank accounts for a user
    @Override
    public ResponseEntity<ResponsePayload<List<BankAccount>>> viewAllBankAccounts(LoggingParameter loggingParameter, String key) {
        // Retrieve the current user's session using the key
        CurrentUserSession currUserSession = currentSessionRepo.findByToken(key);
        if(currUserSession==null) {
            throw new ApplicationException(StatusCode.UNAUTHORIZED, loggingParameter.getRequestId(), StatusMessage.NO_LOGGED_IN_CUSTOMER, StatusMessage.NO_LOGGED_IN_CUSTOMER);
        }
        List<BankAccount> bankAccounts = bankAccountRepo.findAllByWallet(walletRepo.showCustomerWalletDetails(currUserSession.getUserId()).getWalletId());
        if (bankAccounts.isEmpty()){
            throw new ApplicationException(StatusCode.BAD_REQUEST, loggingParameter.getRequestId(), StatusMessage.BANK_NOT_FOUND, StatusMessage.BANK_NOT_FOUND);
        }
        ApiResponse.Header header = new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.OK, StatusMessage.SUCCESS, StatusMessage.SUCCESS, LocalDateTime.now().toString());
        return ResponseEntity.ok(new ResponsePayload<>(header, bankAccounts));
    }
}
