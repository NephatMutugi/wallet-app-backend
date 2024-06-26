package org.basalt.main.transactions.repository;

import org.basalt.main.transactions.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
public interface TransactionsRepo extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByTransactionType(String transactionType);

    List<Transaction> findTransactionByWalletId(UUID walletId);

    List<Transaction> findTransactionByTransactionDate(Date transactionDate);

    List<Transaction> findByTransactionDateBetween(Date startSate, Date endDate);
}
