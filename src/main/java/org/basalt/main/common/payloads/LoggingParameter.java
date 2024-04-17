package org.basalt.main.common.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ Author Nephat Muchiri
 * Date 13/04/2024
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoggingParameter {
    private String requestId;
    private String sessionId;
    private String identifier;
    private String sourceApp;
}
