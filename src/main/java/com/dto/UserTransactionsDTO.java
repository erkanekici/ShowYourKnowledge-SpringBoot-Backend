package com.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserTransactionsDTO extends BaseDTO{

    private Long id;
    private String transactionId;
    private Long userId;
    private String serviceName;
    private String methodName;
    private String request;
    private String response;
    private String resultCode;
    private String errorCode;
    private String errorMessage;
    private String clientIp;

}
