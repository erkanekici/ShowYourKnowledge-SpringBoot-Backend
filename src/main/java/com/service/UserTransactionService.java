package com.service;

import com.dto.UserTransactionsDTO;

public interface UserTransactionService {
    Long saveRequestLog(UserTransactionsDTO userTransactionsDTO);
    void saveSuccessfulResponseLog(Long id, Long userId, String response);
    void saveFailedResponseLog(Long id, Long userId, String response, String errorCode, String errorMessage);
    boolean isValidTransactionId(String transactionId, Long userId);
}
