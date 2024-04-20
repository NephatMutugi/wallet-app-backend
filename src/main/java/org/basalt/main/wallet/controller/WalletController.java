package org.basalt.main.wallet.controller;

import org.basalt.main.common.payloads.LoggingParameter;
import org.basalt.main.common.payloads.ResponsePayload;
import org.basalt.main.common.utils.CommonUtils;
import org.basalt.main.wallet.model.dto.FundsTransferRequest;
import org.basalt.main.wallet.model.dto.FundsTransferResponse;
import org.basalt.main.wallet.model.dto.WalletDto;
import org.basalt.main.wallet.service.WalletService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ Author Nephat Muchiri
 * Date 20/04/2024
 */
@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {
    private final WalletService walletService;
    private final CommonUtils commonUtils;

    public WalletController(WalletService walletService, CommonUtils commonUtils) {
        this.walletService = walletService;
        this.commonUtils = commonUtils;
    }

    /**
     * Endpoint to transfer funds between wallets.
     *
     * @param headers HTTP headers for extracting necessary metadata.
     * @param key     A unique session or user key for validation.
     * @param mobileNumber Mobile Number associated to the given wallet
     * @return A ResponseEntity containing the funds transfer response.
     */
    @GetMapping("balance")
    ResponseEntity<ResponsePayload<WalletDto>> getWalletBalance(
            @RequestHeader HttpHeaders headers,
            @RequestParam String key,
            @RequestParam String mobileNumber
    ) {
        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        return walletService.showBalance(loggingParameter, mobileNumber, key);
    }

    /**
     * Endpoint to transfer funds between wallets.
     *
     * @param headers HTTP headers for extracting necessary metadata.
     * @param key     A unique session or user key for validation.
     * @param request The funds transfer request details.
     * @return A ResponseEntity containing the funds transfer response.
     */
    @PostMapping("/transfer")
    public ResponseEntity<ResponsePayload<FundsTransferResponse>> fundTransfer(
            @RequestHeader HttpHeaders headers,
            @RequestParam String key,
            @RequestBody FundsTransferRequest request) {

        LoggingParameter loggingParameter = commonUtils.validateHeaders(headers);
        // Execute the transfer
        return walletService.fundTransfer(loggingParameter, key, request);

    }

}
