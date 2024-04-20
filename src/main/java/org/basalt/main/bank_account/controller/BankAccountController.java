package org.basalt.main.bank_account.controller;

import org.basalt.main.bank_account.model.BankAccount;
import org.basalt.main.bank_account.model.dto.BankAccountDTO;
import org.basalt.main.bank_account.service.BankAccountService;
import org.basalt.main.common.payloads.LoggingParameter;
import org.basalt.main.common.payloads.ResponsePayload;
import org.basalt.main.common.utils.CommonUtils;
import org.basalt.main.wallet.model.Wallet;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
@RestController
@RequestMapping("/api/v1/bank-account")
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final CommonUtils commonUtils;

    public BankAccountController(BankAccountService bankAccountService, CommonUtils commonUtils) {
        this.bankAccountService = bankAccountService;
        this.commonUtils = commonUtils;
    }

    // Endpoint to add a new bank account using POST method
    @PostMapping("/add")
    public ResponseEntity<ResponsePayload<BankAccount>> addBankAccount(
            @RequestHeader HttpHeaders headers,
            @RequestParam String key,
            @RequestBody BankAccountDTO request) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return bankAccountService.addBankAccount(loggingParameter, key, request);
    }

    // Endpoint to remove a bank account using POST method
    @PostMapping("/remove")
    public ResponseEntity<ResponsePayload<Wallet>> removeBankAccount(
            @RequestHeader HttpHeaders headers,
            @RequestParam String key,
            @RequestBody BankAccountDTO request) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return bankAccountService.removeBankAccount(loggingParameter, key, request);
    }

    // Endpoint to view a single bank account details using GET method
    @GetMapping("/view")
    public ResponseEntity<ResponsePayload<BankAccount>> viewBankAccount(
            @RequestHeader HttpHeaders headers,
            @RequestParam String key,
            @RequestParam long accountNo) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return bankAccountService.viewBankAccount(loggingParameter, key, accountNo);
    }

    // Endpoint to view all bank accounts for a user using GET method
    @GetMapping("/view-all")
    public ResponseEntity<ResponsePayload<List<BankAccount>>> viewAllBankAccounts(
            @RequestHeader HttpHeaders headers,
            @RequestParam String key) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return bankAccountService.viewAllBankAccounts(loggingParameter, key);
    }
}
