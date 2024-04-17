package org.basalt.main.common.exceptions;

import org.basalt.main.common.payloads.ApiResponse;
import org.basalt.main.common.utils.CommonUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 13/04/2024
 */
@RestControllerAdvice
public class GlobalHandler extends ResponseEntityExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR = "500";
    private static final String BAD_REQUEST = "400";
    private static final String CUSTOMER_SERVICE_UNAVAILABLE = "Customer Service Unavailable";

    public static ApiResponse getApiResponse(String code, String message, String customerMsg, String requestId) {
        ApiResponse apiResponse = new ApiResponse();
        ApiResponse.Header header = new ApiResponse.Header();
        header.setRequestRefId(requestId);
        header.setResponseCode(code);
        header.setResponseMessage(message);
        header.setCustomerMessage(customerMsg);
        header.setTimestamp(LocalDateTime.now().toString());
        apiResponse.setHeader(header);
        return apiResponse;
    }

    @ExceptionHandler(ApplicationException.class)
    public Mono<ResponseEntity<ApiResponse>> handle(ApplicationException exception) {

        return Mono.just(ResponseEntity.status(Integer.parseInt(exception.getStatusCode())).body(
                getApiResponse(exception.getStatusCode(), exception.getDeveloperMessage(), exception.getCustomerMessage(), exception.getRequestId())
        ));
    }

    @Override
    protected Mono<ResponseEntity<Object>> handleMethodNotAllowedException(MethodNotAllowedException ex, HttpHeaders headers, HttpStatusCode status, ServerWebExchange exchange) {

        return Mono.just(ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
                getApiResponse(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()), ex.getMessage(), CUSTOMER_SERVICE_UNAVAILABLE, CommonUtils.getRequestRefId(exchange))
        ));    }



    @Override
    public Mono<ResponseEntity<Object>> handleNotAcceptableStatusException(
            NotAcceptableStatusException ex, HttpHeaders headers, HttpStatusCode httpStatusCode, ServerWebExchange exchange) {

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
                getApiResponse(BAD_REQUEST, ex.getMessage(), CUSTOMER_SERVICE_UNAVAILABLE, CommonUtils.getRequestRefId(exchange))
        ));
    }

    @Override
    public Mono<ResponseEntity<Object>> handleUnsupportedMediaTypeStatusException(
            UnsupportedMediaTypeStatusException ex, HttpHeaders headers, HttpStatusCode httpStatusCode, ServerWebExchange exchange) {

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
                getApiResponse(BAD_REQUEST, ex.getMessage(), CUSTOMER_SERVICE_UNAVAILABLE, CommonUtils.getRequestRefId(exchange))
        ));
    }

    @Override
    public Mono<ResponseEntity<Object>> handleMissingRequestValueException(
            MissingRequestValueException ex, HttpHeaders headers, HttpStatusCode httpStatusCode, ServerWebExchange exchange) {

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
                getApiResponse(BAD_REQUEST, ex.getMessage(), CUSTOMER_SERVICE_UNAVAILABLE, CommonUtils.getRequestRefId(exchange))
        ));
    }

    @Override
    protected Mono<ResponseEntity<Object>> handleUnsatisfiedRequestParameterException(
            UnsatisfiedRequestParameterException ex, HttpHeaders headers, HttpStatusCode httpStatusCode, ServerWebExchange exchange) {

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
                getApiResponse(BAD_REQUEST, ex.getMessage(), CUSTOMER_SERVICE_UNAVAILABLE, CommonUtils.getRequestRefId(exchange))
        ));
    }

    @Override
    public Mono<ResponseEntity<Object>> handleWebExchangeBindException(
            WebExchangeBindException ex, HttpHeaders headers, HttpStatusCode httpStatusCode, ServerWebExchange exchange) {
        String errorMessage;

        FieldError fieldError = ex.getFieldError();

        if (Objects.isNull(fieldError))
            errorMessage = ex.getMessage();
        else
            errorMessage = fieldError.getDefaultMessage();

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
                getApiResponse(BAD_REQUEST, errorMessage, CUSTOMER_SERVICE_UNAVAILABLE, CommonUtils.getRequestRefId(exchange))
        ));
    }

    @Override
    public Mono<ResponseEntity<Object>> handleServerWebInputException(
            ServerWebInputException ex, HttpHeaders headers, HttpStatusCode httpStatusCode, ServerWebExchange exchange) {

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
                getApiResponse(BAD_REQUEST, ex.getMessage(), CUSTOMER_SERVICE_UNAVAILABLE, CommonUtils.getRequestRefId(exchange))
        ));
    }

    @Override
    public Mono<ResponseEntity<Object>> handleResponseStatusException(
            ResponseStatusException ex, HttpHeaders headers, HttpStatusCode httpStatusCode, ServerWebExchange exchange) {

        return Mono.just(ResponseEntity.status(httpStatusCode.value()).body(
                getApiResponse(httpStatusCode.toString(), ex.getMessage(), CUSTOMER_SERVICE_UNAVAILABLE, CommonUtils.getRequestRefId(exchange))
        ));
    }

    @Override
    public Mono<ResponseEntity<Object>> handleServerErrorException(
            ServerErrorException ex, HttpHeaders headers, HttpStatusCode httpStatusCode, ServerWebExchange exchange) {

        return Mono.just(ResponseEntity.status(httpStatusCode.value()).body(
                getApiResponse(httpStatusCode.toString(), ex.getMessage(), CUSTOMER_SERVICE_UNAVAILABLE, CommonUtils.getRequestRefId(exchange))
        ));
    }

    @Override
    public Mono<ResponseEntity<Object>> handleErrorResponseException(
            ErrorResponseException ex, HttpHeaders headers, HttpStatusCode httpStatusCode, ServerWebExchange exchange) {

        return Mono.just(ResponseEntity.status(httpStatusCode.value()).body(
                getApiResponse(httpStatusCode.toString(), ex.getMessage(), CUSTOMER_SERVICE_UNAVAILABLE, CommonUtils.getRequestRefId(exchange))
        ));
    }

    @Override
    protected Mono<ResponseEntity<Object>> handleMethodValidationException(MethodValidationException ex, HttpStatus status, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.status(Integer.parseInt(INTERNAL_SERVER_ERROR)).body(
                getApiResponse(INTERNAL_SERVER_ERROR, ex.getMessage(), CUSTOMER_SERVICE_UNAVAILABLE, UUID.randomUUID().toString())
        ));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Object>> handle(Exception exception) {

        return Mono.just(ResponseEntity.status(Integer.parseInt(INTERNAL_SERVER_ERROR)).body(
                getApiResponse(INTERNAL_SERVER_ERROR, exception.getMessage(), CUSTOMER_SERVICE_UNAVAILABLE, UUID.randomUUID().toString())
        ));
    }
}
