package org.basalt.main.customer.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
@Data
@Builder
public class PasswordResetDto {
    private UUID customerId;
    private String previousPassword;
    private String newPassword;
}
