package org.basalt.main.customer.controller;

import org.basalt.main.common.payloads.LoggingParameter;
import org.basalt.main.common.payloads.ResponsePayload;
import org.basalt.main.common.utils.CommonUtils;
import org.basalt.main.customer.model.Customer;
import org.basalt.main.customer.model.dto.CustomerDto;
import org.basalt.main.customer.model.dto.PasswordResetDto;
import org.basalt.main.customer.service.CustomerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ Author Nephat Muchiri
 * Date 18/04/2024
 */
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {


    private final CustomerService customerService;
    private final CommonUtils commonUtils;

    public CustomerController(CustomerService customerService, CommonUtils commonUtils) {
        this.customerService = customerService;
        this.commonUtils = commonUtils;
    }

    // Endpoint to create a new customer account
    @PostMapping("/create")
    public ResponseEntity<ResponsePayload<CustomerDto>> createCustomerAccount(
            @RequestHeader HttpHeaders headers,
            @RequestBody Customer request) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return customerService.createCustomerAccount(loggingParameter, request);
    }

    // Endpoint to update an existing customer
    @PostMapping("/update")
    public ResponseEntity<ResponsePayload<Customer>> updateCustomer(
            @RequestParam String key,
            @RequestHeader HttpHeaders headers,
            @RequestBody Customer request) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return customerService.updateCustomer(loggingParameter, key, request);
    }

    // Endpoint to update the status of a customer
    @PostMapping("/update-status")
    public ResponseEntity<ResponsePayload<Customer>> updateCustomerStatus(
            @RequestParam String key,
            @RequestHeader HttpHeaders headers,
            @RequestBody Customer request) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return customerService.updateCustomerStatus(loggingParameter, key, request);
    }

    // Endpoint for resetting a customer's password
    @PostMapping("/reset-password")
    public ResponseEntity<ResponsePayload<Customer>> resetPassword(
            @RequestParam String key,
            @RequestHeader HttpHeaders headers,
            @RequestBody PasswordResetDto request) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return customerService.resetPassword(loggingParameter, key, request);
    }
}
