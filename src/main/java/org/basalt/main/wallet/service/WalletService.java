package org.basalt.main.wallet.service;

import org.basalt.main.common.payloads.LoggingParameter;
import org.basalt.main.common.payloads.ResponsePayload;
import org.basalt.main.wallet.model.dto.FundsTransferRequest;
import org.basalt.main.wallet.model.dto.FundsTransferResponse;
import org.basalt.main.wallet.model.dto.WalletDto;
import org.springframework.http.ResponseEntity;

/**
 * @ Author Nephat Muchiri
 * Date 18/04/2024
 */
public interface WalletService {
    ResponseEntity<ResponsePayload<WalletDto>> showBalance(LoggingParameter loggingParameter, String mobile, String key);
    ResponseEntity<ResponsePayload<FundsTransferResponse>> fundTransfer(LoggingParameter loggingParameter, String key, FundsTransferRequest request);
}
