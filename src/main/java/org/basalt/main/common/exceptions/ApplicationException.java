package org.basalt.main.common.exceptions;

import lombok.Getter;

import java.io.Serial;

/**
 * @ Author Nephat Muchiri
 * Date 13/04/2024
 */
@Getter
public class ApplicationException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 2649660654896662250L;

    private final String statusCode;
    private final String requestId;
    private final String developerMessage;
    private final String customerMessage;

    public ApplicationException(String statusCode, String requestId, String developerMessage, String customerMessage) {
        super();
        this.statusCode = statusCode;
        this.requestId = requestId;
        this.developerMessage = developerMessage;
        this.customerMessage = customerMessage;
    }
}
