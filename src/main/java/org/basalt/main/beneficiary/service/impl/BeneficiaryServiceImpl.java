package org.basalt.main.beneficiary.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.basalt.main.beneficiary.model.Beneficiary;
import org.basalt.main.beneficiary.model.dto.BeneficiaryDTO;
import org.basalt.main.beneficiary.repository.BeneficiaryRepo;
import org.basalt.main.beneficiary.service.BeneficiaryService;
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
import org.basalt.main.wallet.model.Wallet;
import org.basalt.main.wallet.repository.WalletRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.basalt.main.common.utils.StatusCode.*;
import static org.basalt.main.common.utils.StatusMessage.*;
import static org.basalt.main.common.utils.StatusMessage.DUPLICATE_RECORD;

/**
 * @ Author Nephat Muchiri
 * Date 18/04/2024
 */
@Service
@Slf4j
public class BeneficiaryServiceImpl implements BeneficiaryService {
    private final BeneficiaryRepo beneficiaryRepo;
    private final CurrentSessionRepo currentSessionRepo;
    private final CustomerRepo customerRepo;
    private final WalletRepo walletRepo;

    public BeneficiaryServiceImpl(BeneficiaryRepo beneficiaryRepo, CurrentSessionRepo currentSessionRepo, CustomerRepo customerRepo, WalletRepo walletRepo) {
        this.beneficiaryRepo = beneficiaryRepo;
        this.currentSessionRepo = currentSessionRepo;
        this.customerRepo = customerRepo;
        this.walletRepo = walletRepo;
    }

    @Override
    public ResponseEntity<ResponsePayload<Beneficiary>> addBeneficiary(LoggingParameter loggingParameter, Beneficiary request, String key) {
        CurrentUserSession currUserSession = currentSessionRepo.findByToken(key);
        if(currUserSession==null) {
            throw new ApplicationException(StatusCode.UNAUTHORIZED, loggingParameter.getRequestId(), StatusMessage.NO_LOGGED_IN_CUSTOMER, StatusMessage.NO_LOGGED_IN_CUSTOMER);
        }

        Optional<Customer> customerOptional = customerRepo.findById(currUserSession.getUserId());
        if (customerOptional.isEmpty()){
            log.error("CUSTOMER NOT FOUND: {}",currUserSession.getUserId());
            throw new ApplicationException(NOT_FOUND, loggingParameter.getRequestId(), ERROR_ADD_BENEFICIARIES, ERROR_ADD_BENEFICIARIES);
        }

        Wallet wallet = walletRepo.showCustomerWalletDetails(customerOptional.get().getCustomerId());
        if (wallet == null){
            throw new ApplicationException(NOT_FOUND, loggingParameter.getRequestId(), WALLET_NOT_FOUND, WALLET_NOT_FOUND);
        }

        Beneficiary beneficiary = beneficiaryRepo.findBeneficiaryByBeneficiaryMobileNumber(request.getBeneficiaryMobileNumber());
        if (!(beneficiary == null)){
            throw new ApplicationException(BAD_REQUEST, loggingParameter.getRequestId(), DUPLICATE_RECORD, DUPLICATE_RECORD);
        }

        ApiResponse.Header header = new ApiResponse.Header(loggingParameter.getRequestId(), OK, SUCCESS, SUCCESS, LocalDateTime.now().toString());
        return ResponseEntity.ok(new ResponsePayload<>(header, beneficiaryRepo.save(request)));
    }

    @Override
    public ResponseEntity<ResponsePayload<List<Beneficiary>>> findAllByWallet(LoggingParameter loggingParameter, UUID walletId) {
        List<Beneficiary> beneficiaries = beneficiaryRepo.findBeneficiariesByWalletId(walletId);
        if (beneficiaries.isEmpty()){
            throw new ApplicationException(NOT_FOUND, loggingParameter.getRequestId(), RECORD_NOT_FOUND, RECORD_NOT_FOUND);
        }
        ApiResponse.Header header = new ApiResponse.Header(loggingParameter.getRequestId(), OK, SUCCESS, SUCCESS, LocalDateTime.now().toString());
        return ResponseEntity.ok(new ResponsePayload<>(header, beneficiaries));
    }

    @Override
    public ResponseEntity<ResponsePayload<Beneficiary>> viewBeneficiary(LoggingParameter loggingParameter, String beneficiaryName, String key) {
        CurrentUserSession currUserSession = currentSessionRepo.findByToken(key);
        if(currUserSession==null) {
            throw new ApplicationException(StatusCode.UNAUTHORIZED, loggingParameter.getRequestId(), StatusMessage.NO_LOGGED_IN_CUSTOMER, StatusMessage.NO_LOGGED_IN_CUSTOMER);
        }

        Wallet wallet = walletRepo.showCustomerWalletDetails(currUserSession.getUserId());
        if (wallet == null){
            log.error("WALLET NOT FOUND");
            throw new ApplicationException(NOT_FOUND, loggingParameter.getRequestId(), RECORD_NOT_FOUND, RECORD_NOT_FOUND);
        }
        Beneficiary beneficiary = beneficiaryRepo.findBeneficiaryByBeneficiaryNameAndWalletId(beneficiaryName, wallet.getWalletId());
        if (beneficiary == null){
            log.error("BENEFICIARY NOT FOUND");
            throw new ApplicationException(NOT_FOUND, loggingParameter.getRequestId(), RECORD_NOT_FOUND, RECORD_NOT_FOUND);
        }
        ApiResponse.Header header = new ApiResponse.Header(loggingParameter.getRequestId(), OK, SUCCESS, SUCCESS, LocalDateTime.now().toString());
        return ResponseEntity.ok(new ResponsePayload<>(header, beneficiary));
    }

    @Override
    public ResponseEntity<ResponsePayload<List<Beneficiary>>> viewAllBeneficiary(LoggingParameter loggingParameter, String key) {
        CurrentUserSession currUserSession = currentSessionRepo.findByToken(key);
        if(currUserSession==null) {
            throw new ApplicationException(UNAUTHORIZED, loggingParameter.getRequestId(), NO_LOGGED_IN_CUSTOMER, NO_LOGGED_IN_CUSTOMER);
        }
        List<Beneficiary> beneficiaries = beneficiaryRepo.findAll();
        if (beneficiaries.isEmpty()){
            log.error("BENEFICIARIES NOT FOUND");
            throw new ApplicationException(NOT_FOUND, loggingParameter.getRequestId(), RECORD_NOT_FOUND, RECORD_NOT_FOUND);
        }
        ApiResponse.Header header = new ApiResponse.Header(loggingParameter.getRequestId(), OK, SUCCESS, SUCCESS, LocalDateTime.now().toString());
        return ResponseEntity.ok(new ResponsePayload<>(header, beneficiaries));
    }

    @Override
    public ResponseEntity<ResponsePayload<Beneficiary>> deleteBeneficiary(LoggingParameter loggingParameter, String key, BeneficiaryDTO request) {
        CurrentUserSession currUserSession = currentSessionRepo.findByToken(key);
        if(currUserSession==null) {
            throw new ApplicationException(UNAUTHORIZED, loggingParameter.getRequestId(), NO_LOGGED_IN_CUSTOMER, NO_LOGGED_IN_CUSTOMER);
        }

        try {
            Wallet wallet = walletRepo.showCustomerWalletDetails(currUserSession.getUserId());
            Beneficiary beneficiary = beneficiaryRepo.findBeneficiaryByWalletIdAndBeneficiaryMobileNumber(wallet.getWalletId(), request.getBeneficiaryMobileNumber());
            beneficiaryRepo.delete(beneficiary);
            ApiResponse.Header header = new ApiResponse.Header(loggingParameter.getRequestId(), OK, "Deleted Successfully", "Deleted Successfully", LocalDateTime.now().toString());
            return ResponseEntity.ok(new ResponsePayload<>(header, beneficiary));
        } catch (Exception e){
            throw new ApplicationException(BAD_REQUEST, loggingParameter.getRequestId(), RECORD_NOT_FOUND, RECORD_NOT_FOUND);

        }

    }
}
