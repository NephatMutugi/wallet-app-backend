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
import org.basalt.main.customer.model.Login;
import org.basalt.main.customer.repository.CurrentSessionRepo;
import org.basalt.main.customer.repository.CustomerRepo;
import org.basalt.main.customer.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.basalt.main.customer.service.ServiceUtils.generateSecureToken;
import static org.basalt.main.customer.service.ServiceUtils.isPasswordValid;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    private final CurrentSessionRepo currentSessionRepo;
    private final CustomerRepo customerRepo;

    public LoginServiceImpl(CurrentSessionRepo currentSessionRepo, CustomerRepo customerRepo) {
        this.currentSessionRepo = currentSessionRepo;
        this.customerRepo = customerRepo;
    }

    /**
     * Logs in a user by verifying credentials and creating a new session if successful.
     */
    @Override
    public ResponseEntity<ResponsePayload<CurrentUserSession>> login(LoggingParameter loggingParameter, Login request) {
        Customer existingCustomer = customerRepo.findCustomerByMobileNumber(request.getMobileNumber());
        if (existingCustomer == null){
            throw new ApplicationException(StatusCode.UNAUTHORIZED, loggingParameter.getRequestId(), StatusMessage.CUSTOMER_MSISDN_NOT_FOUND, StatusMessage.CUSTOMER_MSISDN_NOT_FOUND);
        }

        CurrentUserSession session = currentSessionRepo.findCurrentUserSessionByUserId(existingCustomer.getCustomerId());
        if (!(session == null)) {
            throw new ApplicationException(StatusCode.BAD_REQUEST, loggingParameter.getRequestId(), "This user has another active session", "This user has another active session");
        }

        // Checking password validity
        if (!isPasswordValid(request.getPassword(), existingCustomer.getPassword())){
            throw new ApplicationException(StatusCode.UNAUTHORIZED, loggingParameter.getRequestId(), StatusMessage.WRONG_PASSWORD, StatusMessage.WRONG_PASSWORD);
        }

        // Generate secure token
        String token = generateSecureToken();
        CurrentUserSession currentUserSession = CurrentUserSession.builder()
                .userId(existingCustomer.getCustomerId())
                .token(token).build();

        // UPDATE LAST LOGIN DATE
        existingCustomer.setLastLoginDate(LocalDateTime.now());
        customerRepo.save(existingCustomer);

        currentUserSession = currentSessionRepo.save(currentUserSession);
        ApiResponse.Header header = new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.OK, StatusMessage.SUCCESS, StatusMessage.SUCCESS, LocalDateTime.now().toString());
        return ResponseEntity.ok(new ResponsePayload<>(header, currentUserSession));
    }

    @Override
    public ResponseEntity<ResponsePayload<String>> logout(LoggingParameter loggingParameter, String key) {
        CurrentUserSession currentUserSession = currentSessionRepo.findByToken(key);
        if (currentUserSession == null){
            throw new ApplicationException(StatusCode.BAD_REQUEST, loggingParameter.getRequestId(), StatusMessage.INVALID_TOKEN, StatusMessage.INVALID_TOKEN);
        }
        currentSessionRepo.delete(currentUserSession);
        ApiResponse.Header header = new ApiResponse.Header(loggingParameter.getRequestId(), StatusCode.OK, StatusMessage.SUCCESS, StatusMessage.SUCCESS, LocalDateTime.now().toString());
        return ResponseEntity.ok(new ResponsePayload<>(header, StatusMessage.LOGOUT_SUCCESS));
    }
}
