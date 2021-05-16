package com.controller.handler;

import javax.servlet.http.HttpServletRequest;

public interface UserTransactionHandler {

    Long logRequest(HttpServletRequest request, String transactionId, Long userId, String serviceName, String methodName, String requestBody);
    void logSuccessfulResponse(Long id, Long userId, String response);
    void logFailedResponse(Long id, Long userId, String response, String errorCode, String errorMessage);
    boolean isValidTransactionId(String transactionId, Long userId);
}
