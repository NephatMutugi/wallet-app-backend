package org.basalt.main.customer.service;

import org.basalt.main.common.payloads.LoggingParameter;
import org.basalt.main.common.payloads.ResponsePayload;
import org.basalt.main.customer.model.Customer;
import org.basalt.main.customer.model.dto.CustomerDto;
import org.basalt.main.customer.model.dto.PasswordResetDto;
import org.springframework.http.ResponseEntity;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
public interface CustomerService {
    ResponseEntity<ResponsePayload<CustomerDto>> createCustomerAccount(LoggingParameter loggingParameter, Customer request);
    ResponseEntity<ResponsePayload<CustomerDto>> findCustomerByMobileNumber(LoggingParameter loggingParameter, String mobileNumber);
    ResponseEntity<ResponsePayload<Customer>> updateCustomer(LoggingParameter loggingParameter, String key, Customer request);
    ResponseEntity<ResponsePayload<Customer>> updateCustomerStatus(LoggingParameter loggingParameter, String key, Customer request);
    ResponseEntity<ResponsePayload<Customer>> resetPassword(LoggingParameter loggingParameter, String key, PasswordResetDto request);
}
