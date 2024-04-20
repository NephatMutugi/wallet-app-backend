package org.basalt.main.wallet.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.basalt.main.customer.model.Customer;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 18/04/2024
 */
@Data
@Builder
@ToString
public class WalletDto {
    private UUID walletId;
    private BigDecimal balance;
    private Customer customer;

}
