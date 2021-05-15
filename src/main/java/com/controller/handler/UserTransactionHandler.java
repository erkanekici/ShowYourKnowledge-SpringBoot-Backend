package com.controller.handler;

import javax.servlet.http.HttpServletRequest;

public interface UserTransactionHandler {

    Long logRequest(HttpServletRequest request, String transactionId, Long userId, String serviceName, String methodName, String requestBody);
    void logResponse(Long id, Long userId, String response);
    boolean isValidTransactionId(String transactionId, Long userId);
}
