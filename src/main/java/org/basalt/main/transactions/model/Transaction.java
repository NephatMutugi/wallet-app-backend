package org.basalt.main.transactions.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.*;
import lombok.*;
import org.basalt.main.common.config.entityaudit.EntityAudit;

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
   @JsonSerialize(using = ToStringSerializer.class)
   private UUID transactionId;
   private String transactionType;
   private LocalDate transactionDate;
   private BigDecimal amount;
   private String Description;
   @JsonSerialize(using = ToStringSerializer.class)
   private UUID walletId;

}

