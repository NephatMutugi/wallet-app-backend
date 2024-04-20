package org.basalt.main.wallet.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ Author Nephat Muchiri
 * Date 20/04/2024
 */
@Data
public class FundsTransferRequest {
    private String transactionType;
    private String from;
    private String to;
    private BigDecimal amount;
}
