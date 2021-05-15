package com.service;

import com.dto.UserTransactionsDTO;

public interface UserTransactionService {
    Long saveRequestLog(UserTransactionsDTO userTransactionsDTO);
    void saveResponseLog(Long id, Long userId, String response);
    boolean isValidTransactionId(String transactionId, Long userId);
}
