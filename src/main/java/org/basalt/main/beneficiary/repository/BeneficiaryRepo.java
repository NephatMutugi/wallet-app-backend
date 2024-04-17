package org.basalt.main.beneficiary.repository;

import org.basalt.main.beneficiary.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
public interface BeneficiaryRepo extends JpaRepository<Beneficiary, UUID> {

    Beneficiary findBeneficiaryByBeneficiaryMobileNumber(String mobileNumber);
    Beneficiary findBeneficiaryByBeneficiaryNameAndWalletId(String beneficiaryName, UUID walletId);
    Beneficiary findBeneficiaryByWalletIdAndBeneficiaryMobileNumber(UUID walletId, String mobileNumber);
    List<Beneficiary> findBeneficiariesByWalletId(UUID walletId);
}
