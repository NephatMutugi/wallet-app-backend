package org.basalt.main.wallet.repository;

import org.basalt.main.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
public interface WalletRepo extends JpaRepository<Wallet, UUID> {
    @Query("FROM Wallet w INNER JOIN w.customer c WHERE c=?1")
    Wallet showCustomerWalletDetails(UUID customerId);
}
