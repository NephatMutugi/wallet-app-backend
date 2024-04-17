package org.basalt.main.wallet.model;

import jakarta.persistence.*;
import lombok.*;
import org.basalt.main.customer.model.Customer;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "walletId", nullable = false)
    private UUID walletId;

    private BigDecimal balance;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customer_Id")
    private Customer customer;



}
