package org.basalt.main.wallet.model.dto;

import lombok.*;
import org.basalt.main.wallet.model.Wallet;

import java.math.BigDecimal;

/**
 * @ Author Nephat Muchiri
 * Date 20/04/2024
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FundsTransferResponse {
    private BigDecimal transactionAmount;
    private Wallet body;

}
