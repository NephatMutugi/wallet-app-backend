package org.basalt.main.transactions.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

   private UUID transactionId;

   private String transactionType;

   private LocalDate transactionDate;

   private BigDecimal amount;

   private String Description;

}