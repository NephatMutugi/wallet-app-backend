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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse {

    protected Header header;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Header{
        private String requestRefId;
        private String responseCode;
        private String responseMessage;
        private String customerMessage;
        private String timestamp;
    }
}
