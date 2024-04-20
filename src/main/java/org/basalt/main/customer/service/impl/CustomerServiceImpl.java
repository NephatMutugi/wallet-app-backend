package org.basalt.main.customer.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.basalt.main.common.exceptions.ApplicationException;
import org.basalt.main.common.payloads.ApiResponse;
import org.basalt.main.common.payloads.LoggingParameter;
import org.basalt.main.common.payloads.ResponsePayload;
import org.basalt.main.common.utils.StatusCode;
import org.basalt.main.common.utils.StatusMessage;
import org.basalt.main.customer.model.CurrentUserSession;
import org.basalt.main.customer.model.Customer;
import org.basalt.main.customer.model.dto.AccountStatus;
import org.basalt.main.customer.model.dto.CustomerDto;
import org.basalt.main.customer.model.dto.PasswordResetDto;
import org.basalt.main.customer.repository.CurrentSessionRepo;
import org.basalt.main.customer.repository.CustomerRepo;
import org.basalt.main.customer.service.CustomerService;
import org.basalt.main.wallet.model.Wallet;
import org.basalt.main.wallet.repository.WalletRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static org.basalt.main.common.utils.CommonUtils.maskEmail;
import static org.basalt.main.common.utils.CommonUtils.maskMSISDN;
import static org.basalt.main.common.utils.StatusCode.*;
import static org.basalt.main.common.utils.StatusMessage.*;
import static org.basalt.main.common.utils.StringConstants.TRANSACTION_LOG;
import static org.basalt.main.customer.service.ServiceUtils.setUpdatedFields;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;
    private final WalletRepo walletRepo;
    private final CurrentSessionRepo currentSessionRepo;

    public CustomerServiceImpl(CustomerRepo customerRepo, WalletRepo walletRepo, CurrentSessionRepo currentSessionRepo) {
        this.customerRepo = customerRepo;
        this.walletRepo = walletRepo;
        this.currentSessionRepo = currentSessionRepo;
    }

    @Override
    public ResponseEntity<ResponsePayload<CustomerDto>> createCustomerAccount(LoggingParameter loggingParameter, Customer request) {
        log.info(TRANSACTION_LOG, "CREATE CUSTOMER...");
        // Check if the @Required fields are present, if not, throw exception
        if (request.getCustomerName() == null|| request.getPassword() == null|| request.getEmail() == null || request.getMobileNumber() == null|| request.getNationalId() == null){
            log.error("MISSING FIELDS WHEN CREATING CUSTOMER: {}", request.getCustomerName());
            throw new ApplicationException(BAD_REQUEST, loggingParameter.getRequestId(), MISSING_FIELDS, MISSING_FIELDS);
        }
        Customer duplicateCustomer = customerRepo.findCustomerByMobileNumberOrEmailOrNationalId(request.getMobileNumber(), request.getEmail(), request.getNationalId());
        // Check if a customer exists (MSISDN, EMAIL, and NATIONAL ID should not exist in the db)
        if (!(duplicateCustomer == null)){
            log.error("DUPLICATE ENTRY: CustomerName: {} | CustomerMobile: {} | CustomerEmail: {}", request.getCustomerName(), maskMSISDN(request.getMobileNumber()), maskEmail(request.getEmail()));
            throw new ApplicationException(BAD_REQUEST, loggingParameter.getRequestId(), DUPLICATE_RECORD, DUPLICATE_RECORD);
        }
        log.info("SAVING CUSTOMER: CustomerName: {} | CustomerMobile: {} | CustomerEmail: {}", request.getCustomerName(), maskMSISDN(request.getMobileNumber()), maskEmail(request.getEmail()));
        request.setAccountStatus("PENDING");
        Customer customer = customerRepo.save(request);


        log.info("CREATE WALLET: CustomerName: {} | CustomerMobile: {} | CustomerEmail: {}", request.getCustomerName(), maskMSISDN(request.getMobileNumber()), maskEmail(request.getEmail()));
        // CREATE A WALLET FOR THE CUSTOMER IMMEDIATELY AFTER REGISTRATION
        Wallet wallet = Wallet.builder()
                .customerId(customer.getCustomerId())
                .balance(BigDecimal.ZERO).build();
        CustomerDto response = toCustomerDto(customer, walletRepo.save(wallet));
        ApiResponse.Header header = new ApiResponse.Header(loggingParameter.getRequestId(), OK, SUCCESS, SUCCESS, LocalDateTime.now().toString());
        return ResponseEntity.ok(new ResponsePayload<>(header, response));
    }

    @Override
    public ResponseEntity<ResponsePayload<CustomerDto>> findCustomerByMobileNumber(LoggingParameter loggingParameter, String mobileNumber) {
        return null;
    }

    @Override
    public ResponseEntity<ResponsePayload<Customer>> updateCustomer(LoggingParameter loggingParameter, String key, Customer request) {
        log.info(TRANSACTION_LOG, "UPDATE CUSTOMER...");

        // Retrieve the current user's session using the key
        CurrentUserSession currUserSession = currentSessionRepo.findByToken(key);
        if(currUserSession==null) {
            throw new ApplicationException(StatusCode.UNAUTHORIZED, loggingParameter.getRequestId(), StatusMessage.NO_LOGGED_IN_CUSTOMER, StatusMessage.NO_LOGGED_IN_CUSTOMER);
        }

        Optional<Customer> customerOptional = customerRepo.findById(request.getCustomerId());
        if (customerOptional.isEmpty()){
            log.error("CUSTOMER NOT FOUND: {}", request.getCustomerName());
            throw new ApplicationException(NOT_FOUND, loggingParameter.getRequestId(), RECORD_NOT_FOUND, RECORD_NOT_FOUND);
        }

        Customer existingCustomer = setUpdatedFields(request, customerOptional.get());
        existingCustomer = customerRepo.save(existingCustomer);
        log.info("UPDATE CUSTOMER: STATUS: {} | CustomerName: {} CustomerMobile: {}", "SUCCESS", existingCustomer.getCustomerName(), maskMSISDN(existingCustomer.getMobileNumber()));
        ApiResponse.Header header = new ApiResponse.Header(loggingParameter.getRequestId(), OK, StatusMessage.SUCCESS, StatusMessage.SUCCESS, LocalDateTime.now().toString());
        return ResponseEntity.ok(new ResponsePayload<>(header, existingCustomer));
    }



    @Override
    public ResponseEntity<ResponsePayload<Customer>> updateCustomerStatus(LoggingParameter loggingParameter, String key, Customer request) {
        log.info(TRANSACTION_LOG, "UPDATE CUSTOMER STATUS...");

        // Retrieve the current user's session using the key
        CurrentUserSession currUserSession = currentSessionRepo.findByToken(key);
        if(currUserSession==null) {
            throw new ApplicationException(StatusCode.UNAUTHORIZED, loggingParameter.getRequestId(), StatusMessage.NO_LOGGED_IN_CUSTOMER, StatusMessage.NO_LOGGED_IN_CUSTOMER);
        }

        Optional<Customer> customerOptional = customerRepo.findById(request.getCustomerId());
        if (customerOptional.isEmpty()){
            throw new ApplicationException(NOT_FOUND, loggingParameter.getRequestId(), RECORD_NOT_FOUND, RECORD_NOT_FOUND);
        }
        Customer existingCustomer = customerOptional.get();
        try {
            // Convert string to enum
            AccountStatus newStatus = AccountStatus.valueOf(request.getAccountStatus().toUpperCase());

            // Check if the new status is different from the current status before setting it
            if (!Objects.equals(existingCustomer.getAccountStatus(), newStatus.toString())) {
                existingCustomer.setAccountStatus(String.valueOf(newStatus));
            }

        } catch (IllegalArgumentException e) {
            log.error("Invalid account status provided: " + request.getAccountStatus(), e);
            throw new ApplicationException(BAD_REQUEST, loggingParameter.getRequestId(), "Invalid account status provided.", "Invalid account status.");
        }

        ApiResponse.Header header = new ApiResponse.Header(loggingParameter.getRequestId(), OK, StatusMessage.SUCCESS, StatusMessage.SUCCESS, LocalDateTime.now().toString());
        return ResponseEntity.ok(new ResponsePayload<>(header, customerRepo.save(existingCustomer)));
    }

    @Override
    public ResponseEntity<ResponsePayload<Customer>> resetPassword(LoggingParameter loggingParameter, String key, PasswordResetDto request) {

        log.info(TRANSACTION_LOG, "RESET PASSWORD REQUEST...");
        // Retrieve the current user's session using the key
        CurrentUserSession currUserSession = currentSessionRepo.findByToken(key);
        if(currUserSession==null) {
            throw new ApplicationException(StatusCode.UNAUTHORIZED, loggingParameter.getRequestId(), StatusMessage.NO_LOGGED_IN_CUSTOMER, StatusMessage.NO_LOGGED_IN_CUSTOMER);
        }

        Optional<Customer> customerOptional = customerRepo.findById(request.getCustomerId());
        if (customerOptional.isEmpty()) {
            throw new ApplicationException(NOT_FOUND, loggingParameter.getRequestId(), "Customer not found", "Customer not found");
        }

        Customer existingCustomer = customerOptional.get();

        // Check if the provided previous password matches the one stored
        if (!Objects.equals(request.getPreviousPassword(), existingCustomer.getPassword())) {
            throw new ApplicationException(UNAUTHORIZED, loggingParameter.getRequestId(), "Invalid password", "Invalid password");
        }

        // Update the password
        existingCustomer.setPassword(request.getNewPassword());
        customerRepo.save(existingCustomer);

        // Delete existing session
        currentSessionRepo.delete(currUserSession);
        ApiResponse.Header header = new ApiResponse.Header(loggingParameter.getRequestId(), OK, "Password updated successfully", "Password updated successfully", LocalDateTime.now().toString());
        return ResponseEntity.ok(new ResponsePayload<>(header, existingCustomer));
    }

    private CustomerDto toCustomerDto(Customer customer, Wallet wallet) {
        return CustomerDto.builder()
                .customerId(customer.getCustomerId())
                .customerName(customer.getCustomerName())
                .mobileNumber(customer.getMobileNumber())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .dateOfBirth(customer.getDateOfBirth())
                .nationalId(customer.getNationalId())
                .accountStatus(customer.getAccountStatus())
                .lastLoginDate(customer.getLastLoginDate())
                .wallet(wallet)
                .build();
    }

}
