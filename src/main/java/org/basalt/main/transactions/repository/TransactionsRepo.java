package org.basalt.main.transactions.repository;

import org.basalt.main.transactions.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
public interface TransactionsRepo extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByTransactionType(String transactionType);

    @Query(value = "FROM Transaction t INNER JOIN t.wallet w WHERE w.walletId=?1")
    List<Transaction> findByWallet(UUID walletId);

    List<Transaction> findByTransactionDate(LocalDate transactionDate);

    List<Transaction> findByTransactionDateBetween(LocalDate startSate, LocalDate endDate);
}
