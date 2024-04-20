package org.basalt.main.customer.model.dto;

import lombok.Builder;
import lombok.Data;
import org.basalt.main.wallet.model.Wallet;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 20/04/2024
 */
@Data
@Builder
public class CustomerDto {

    private UUID customerId;
    private String customerName;
    private String mobileNumber;
    private String email;
    private String address;
    private String dateOfBirth;
    private String nationalId;
    private String accountStatus;
    private LocalDateTime lastLoginDate;
    private Wallet wallet;
}
