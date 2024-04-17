package org.basalt.main.bank_account.repository;

import org.basalt.main.bank_account.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
public interface BankAccountRepo extends JpaRepository<BankAccount, UUID> {

    @Query(value = "FROM BankAccount b INNER JOIN b.wallet w WHERE w.walletId=?1")
    List<BankAccount> findByWallet(UUID walletId);

    @Query(value = "FROM BankAccount b INNER JOIN b.wallet w WHERE w.walletId=?1")
    List<BankAccount> findAllByWallet(UUID walletId);
    BankAccount findByAccountNo(long accountNumber);
}
