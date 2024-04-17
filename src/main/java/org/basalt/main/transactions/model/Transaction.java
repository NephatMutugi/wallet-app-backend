package org.basalt.main.transactions.model;

import jakarta.persistence.*;
import lombok.*;
import org.basalt.main.common.config.entityaudit.EntityAudit;
import org.basalt.main.wallet.model.Wallet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends EntityAudit {

   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   @Column(name = "transactionId", nullable = false, unique = true)
   private UUID transactionId;

   private String transactionType;

   private LocalDate transactionDate;

   private BigDecimal amount;

   private String Description;
   

   @ManyToOne(cascade = CascadeType.ALL)
   private Wallet wallet;

   public Transaction(String transactionType, LocalDate transactionDate, BigDecimal amount, String description) {
      this.transactionType = transactionType;
      this.transactionDate = transactionDate;
      this.amount = amount;
      Description = description;
   }
}

