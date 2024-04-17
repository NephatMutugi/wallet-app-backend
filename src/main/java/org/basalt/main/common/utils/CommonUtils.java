package org.basalt.main.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.basalt.main.common.payloads.LoggingParameter;
import org.basalt.main.common.payloads.ResponsePayload;
import org.basalt.main.common.config.AppProperties;
import org.basalt.main.common.exceptions.ApplicationException;
import org.basalt.main.common.payloads.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @ Author Nephat Muchiri
 * Date 13/04/2024
 */
@Service
public class CommonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);
    private final AppProperties appProperties;

    public CommonUtils(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public LoggingParameter validateHeaders(HttpHeaders headers) {
        validateNotNullHeaders(headers);
        validateConversationId(headers.getFirst(StringConstants.X_CORRELATION_CONVERSATION_ID));
        String requestId = headers.getFirst(StringConstants.X_CORRELATION_CONVERSATION_ID);

        validateAllowedSystems(headers.getFirst(StringConstants.X_SOURCE_APP), requestId);

        return LoggingParameter.builder()
                .sourceApp(headers.getFirst(StringConstants.X_SOURCE_APP))
                .requestId(headers.getFirst(StringConstants.X_CORRELATION_CONVERSATION_ID))
                .build();
    }

    private void validateNotNullHeaders(HttpHeaders headers) {

        if (!headers.containsKey(StringConstants.X_CORRELATION_CONVERSATION_ID)) {
            String requestId = UUID.randomUUID().toString();
            LOGGER.warn("TransactionId={} | ResponseDescription={} ", requestId,
                    "Missing Correlation Identification Item Header");
            throw new ApplicationException(StatusCode.BAD_REQUEST, requestId,
                    StringConstants.X_CORRELATION_CONVERSATION_ID + StatusMessage.HEADER_IS_REQUIRED,
                    StatusMessage.MISSING_REQUIRED_HEADER);
        }

        if (!headers.containsKey(StringConstants.X_SOURCE_APP)) {

            throw new ApplicationException(StatusCode.BAD_REQUEST,
                    headers.getFirst(StringConstants.X_CORRELATION_CONVERSATION_ID),
                    StringConstants.X_SOURCE_APP + StatusMessage.HEADER_IS_REQUIRED,
                    StatusMessage.MISSING_REQUIRED_HEADER);
        }

    }

    private void validateConversationId(String conversationId) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9-]+$");
        Matcher matcher = pattern.matcher(conversationId);
        if (!matcher.matches()) {
            LOGGER.warn("TransactionId={} | ResponseDescription={}", UUID.randomUUID(),
                    "Invalid Correlation Id Header");
            throw new ApplicationException(StatusCode.BAD_REQUEST, conversationId,
                    StatusMessage.INVALID_CONVERSATION_ID, StatusMessage.INVALID_CONVERSATION_ID);
        }
    }

    private void validateAllowedSystems(String input, String requestId) {
        String[] allowed = appProperties.getAllowedSystems().split(",");
        for (String str : allowed) {
            if (str.equalsIgnoreCase(input)) {
                return;
            }
        }
        throw new ApplicationException(StatusCode.BAD_REQUEST, requestId, StatusMessage.INVALID_SOURCE_APP,
                StatusMessage.INVALID_SOURCE_APP);
    }

    public static String getRequestRefId(ServerWebExchange exchange) {
        String requestRefId = UUID.randomUUID().toString();

        // Check if the request id header is present in the header
        if (exchange.getRequest().getHeaders().containsKey(StringConstants.X_CORRELATION_CONVERSATION_ID)) {
            requestRefId = exchange.getRequest().getHeaders().getFirst(StringConstants.X_CORRELATION_CONVERSATION_ID);
        }
        return requestRefId;
    }

    @SneakyThrows
    public static String getJsonString(Object object) {
        return new ObjectMapper().writeValueAsString(object);
    }

    public static Object jsonToObject(String jsonObject, Object inputClass){
        Object object = new Object();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            object = objectMapper.readValue(jsonObject, inputClass.getClass());


        } catch (IOException e){
            throw new ApplicationException(
                    StatusCode.BAD_REQUEST, "", "Failed to convert Json to Object", StatusMessage.CUSTOMER_SERVICE_UNAVAILABLE
            );
        }

        return object;
    }

    public static ApiResponse.Header createHeader(String responseCode, String responseMessage) {
        return new ApiResponse.Header(
                UUID.randomUUID().toString(),  // requestRefId: generate a unique reference for each request
                responseCode,                 // responseCode: 'Success' or 'Error'
                responseMessage,              // responseMessage: description of the outcome
                responseMessage,              // customerMessage: same as responseMessage in this context
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()) // timestamp
        );
    }

    /**
     * Handles Scenarios where queries records do not exist
     *
     * @param loggingParameter the logging parameters used for tracing and logging.
     * @param <T> the type of the response payload content.
     * @return a Mono containing a ResponseEntity with a generic ResponsePayload.
     */
    public static <T> Mono<ResponseEntity<ResponsePayload<T>>> recordNotFoundResponse(LoggingParameter loggingParameter) {
        ApiResponse.Header header = new ApiResponse.Header(
                loggingParameter.getRequestId(),
                StatusCode.NOT_FOUND,
                StatusMessage.RECORD_NOT_FOUND,
                StatusMessage.RECORD_NOT_FOUND,
                Instant.now().toString());
        ResponsePayload<T> responsePayload = new ResponsePayload<>(header, null);
        return Mono.just(ResponseEntity.ok(responsePayload));
    }

    /**
     * Handles illegal state errors generically across multiple services.
     *
     * @param loggingParameter the logging parameters used for tracing and logging.
     * @param e the exception that triggered the error handling.
     * @param <T> the type of the response payload content.
     * @return a Mono containing a ResponseEntity with a generic ResponsePayload.
     */
    public static <T> Mono<ResponseEntity<ResponsePayload<T>>> handleIllegalStateExceptionErrors(LoggingParameter loggingParameter, Throwable e) {
        ApiResponse.Header header = new ApiResponse.Header(
                loggingParameter.getRequestId(),
                StatusCode.OK,
                e.getMessage(),
                StatusMessage.CUSTOMER_SERVICE_UNAVAILABLE,
                Instant.now().toString());

        ResponsePayload<T> responsePayload = new ResponsePayload<>(header, null);
        return Mono.just(ResponseEntity.ok(responsePayload));
    }

    // Masks an MSISDN by leaving the first three and last two digits visible
    public static String maskMSISDN(String msisdn) {
        if (msisdn == null || msisdn.length() < 6) return msisdn; // Return original if too short to mask
        return msisdn.substring(0, 3) + "*****" + msisdn.substring(msisdn.length() - 2);
    }

    // Masks an email by hiding the local-part partially
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email; // Return original if not valid email
        String[] parts = email.split("@");
        String local = parts[0];
        int visibleLength = Math.min(local.length(), 2); // Show at least 2 characters
        return local.substring(0, visibleLength) + "*****" + "@" + parts[1];
    }

    // Masks a generic ID by masking all but the last four characters
    public static String maskID(String id) {
        if (id == null || id.length() < 5) return id; // Return original if too short to mask
        return "****" + id.substring(id.length() - 4);
    }
}
