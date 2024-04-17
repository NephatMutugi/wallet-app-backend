package org.basalt.main.beneficiary.controller;

import org.basalt.main.beneficiary.model.Beneficiary;
import org.basalt.main.beneficiary.model.dto.BeneficiaryDTO;
import org.basalt.main.beneficiary.service.BeneficiaryService;
import org.basalt.main.common.payloads.LoggingParameter;
import org.basalt.main.common.payloads.ResponsePayload;
import org.basalt.main.common.utils.CommonUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 18/04/2024
 */
@RestController
@RequestMapping("api/v1/beneficiary")
public class BeneficiaryController {
    private final BeneficiaryService beneficiaryService;
    private final CommonUtils commonUtils;

    public BeneficiaryController(BeneficiaryService beneficiaryService, CommonUtils commonUtils) {
        this.beneficiaryService = beneficiaryService;
        this.commonUtils = commonUtils;
    }

    // Endpoint to add a new beneficiary
    @PostMapping("/add")
    public ResponseEntity<ResponsePayload<Beneficiary>> addBeneficiary(
            @RequestHeader HttpHeaders headers,
            @RequestBody Beneficiary request,
            @RequestParam String key) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return beneficiaryService.addBeneficiary(loggingParameter, request, key);
    }

    // Endpoint to view a specific beneficiary by name
    @GetMapping("/view")
    public ResponseEntity<ResponsePayload<Beneficiary>> viewBeneficiary(
            @RequestHeader HttpHeaders headers,
            @RequestParam String beneficiaryName,
            @RequestParam String key) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return beneficiaryService.viewBeneficiary(loggingParameter, beneficiaryName, key);
    }

    // Endpoint to view all beneficiaries
    @GetMapping("/view-all")
    public ResponseEntity<ResponsePayload<List<Beneficiary>>> viewAllBeneficiaries(
            @RequestHeader HttpHeaders headers,
            @RequestParam String key) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return beneficiaryService.viewAllBeneficiary(loggingParameter, key);
    }

    // Endpoint to delete a beneficiary
    @PostMapping("/delete")
    public ResponseEntity<ResponsePayload<Beneficiary>> deleteBeneficiary(
            @RequestHeader HttpHeaders headers,
            @RequestBody BeneficiaryDTO request,
            @RequestParam String key) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return beneficiaryService.deleteBeneficiary(loggingParameter, key, request);
    }

    // Endpoint to find all beneficiaries by wallet
    @GetMapping("/find-by-wallet")
    public ResponseEntity<ResponsePayload<List<Beneficiary>>> findAllByWallet(
            @RequestHeader HttpHeaders headers,
            @RequestParam UUID walletId) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return beneficiaryService.findAllByWallet(loggingParameter, walletId);
    }
}
