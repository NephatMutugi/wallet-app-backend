package org.basalt.main.bill_payment.repository;

import org.basalt.main.bill_payment.model.BillPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
public interface BillPaymentRepo extends JpaRepository<BillPayment, UUID> {
}
