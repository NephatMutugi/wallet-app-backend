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
    Wallet findWalletByCustomerId(UUID id);
    Wallet findWalletByWalletId(UUID id);

    @Query("SELECT w FROM Wallet w, Customer c WHERE w.customerId = c.customerId AND c.mobileNumber = :mobileNumber")
    Wallet findWalletByCustomerMobile(String mobileNumber);


}
