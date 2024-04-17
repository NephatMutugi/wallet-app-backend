package org.basalt.main.customer.service;

import org.basalt.main.common.payloads.LoggingParameter;
import org.basalt.main.common.payloads.ResponsePayload;
import org.basalt.main.customer.model.CurrentUserSession;
import org.basalt.main.customer.model.Login;
import org.springframework.http.ResponseEntity;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
public interface LoginService {
    ResponseEntity<ResponsePayload<CurrentUserSession>> login (LoggingParameter loggingParameter, Login login);

    ResponseEntity<ResponsePayload<String>> logout (LoggingParameter loggingParameter, String Key);
}
