package org.basalt.main.customer.controller;

import org.basalt.main.common.payloads.LoggingParameter;
import org.basalt.main.common.payloads.ResponsePayload;
import org.basalt.main.common.utils.CommonUtils;
import org.basalt.main.customer.model.CurrentUserSession;
import org.basalt.main.customer.model.Login;
import org.basalt.main.customer.service.LoginService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {

    private final LoginService loginService;
    private final CommonUtils commonUtils;

    public LoginController(LoginService loginService, CommonUtils commonUtils) {
        this.loginService = loginService;
        this.commonUtils = commonUtils;
    }

    // Endpoint for user login using POST method
    @PostMapping("/login")
    public ResponseEntity<ResponsePayload<CurrentUserSession>> login(
            @RequestHeader HttpHeaders headers,
            @RequestBody Login login) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        Login loginRequest = new Login(login.getMobileNumber(), login.getPassword());
        return loginService.login(loggingParameter, loginRequest);
    }

    // Endpoint for user logout using POST method
    @GetMapping("/logout")
    public ResponseEntity<ResponsePayload<String>> logout(
            @RequestHeader HttpHeaders headers,
            @RequestParam String token) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return loginService.logout(loggingParameter, token);
    }
}
