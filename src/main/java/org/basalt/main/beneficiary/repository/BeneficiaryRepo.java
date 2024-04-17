package org.basalt.main.beneficiary.repository;

import org.basalt.main.beneficiary.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
public interface BeneficiaryRepo extends JpaRepository<Beneficiary, UUID> {
    @Query(value = "FROM Beneficiary b INNER JOIN b.wallet w WHERE w.walletId=?1 AND b.beneficiaryName =?2")
    Beneficiary findByNameWallet(UUID walletId,String beneficiaryName);

    @Query(value = "FROM Beneficiary b INNER JOIN b.wallet w WHERE w.walletId=?1 AND b.beneficiaryMobileNumber =?2")
    Beneficiary findByMobileWallet(UUID walletId,String beneficiaryMobileNumber);

    @Query(value = "FROM Beneficiary b INNER JOIN b.wallet w WHERE w.walletId=?1")
    List<Beneficiary> findByWallet(UUID walletId);
}
