package org.basalt.main.bank_account.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.basalt.main.common.config.entityaudit.EntityAudit;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BankAccount extends EntityAudit {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @NotNull
    @Column(unique = true)
    private long accountNo;

    @NotNull
    @Size(min = 3, max = 10,message = "Invalid Bank Code [ 3-10 Characters only ]")
    private String bankCode;

    @NotNull
    @Size(min = 3, max = 15,message = "Invalid Bank Name [ 3-15 characters only ]")
    private String bankName;

    @NotNull
    private BigDecimal balance;


    @Column(name = "wallet_id")
    private UUID walletId;


    public BankAccount(@NotNull long accountNo, @NotNull String bankCode,
                       @NotNull @Size(min = 4, max = 10, message = "Bank name not valid") String bankName,
                       @NotNull BigDecimal balance) {
        super();
        this.accountNo = accountNo;
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.balance = balance;
    }

}
