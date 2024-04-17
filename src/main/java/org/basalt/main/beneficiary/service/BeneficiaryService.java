package org.basalt.main.beneficiary.service;

import org.basalt.main.beneficiary.model.Beneficiary;
import org.basalt.main.beneficiary.model.dto.BeneficiaryDTO;
import org.basalt.main.common.payloads.LoggingParameter;
import org.basalt.main.common.payloads.ResponsePayload;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 18/04/2024
 */
public interface BeneficiaryService {
    ResponseEntity<ResponsePayload<Beneficiary>> addBeneficiary(LoggingParameter loggingParameter, Beneficiary beneficiary, String key);
    ResponseEntity<ResponsePayload<List<Beneficiary>>> findAllByWallet(LoggingParameter loggingParameter, UUID walletId);
    ResponseEntity<ResponsePayload<Beneficiary>> viewBeneficiary(LoggingParameter loggingParameter, String beneficiaryName, String key);
    ResponseEntity<ResponsePayload<List<Beneficiary>>> viewAllBeneficiary(LoggingParameter loggingParameter, String key);
    ResponseEntity<ResponsePayload<Beneficiary>> deleteBeneficiary(LoggingParameter loggingParameter, String key, BeneficiaryDTO beneficiaryDTO);
}
