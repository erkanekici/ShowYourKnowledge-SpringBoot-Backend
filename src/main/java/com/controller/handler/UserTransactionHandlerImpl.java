package com.controller.handler;

import com.dto.UserTransactionsDTO;
import com.service.UserTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Component
public final class UserTransactionHandlerImpl implements UserTransactionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserTransactionHandlerImpl.class);

    private final UserTransactionService userTransactionService;
    public UserTransactionHandlerImpl(UserTransactionService userTransactionService){
        this.userTransactionService = userTransactionService;
    }

    @Override
    public Long logRequest(HttpServletRequest request, String transactionId, Long userId, String serviceName, String methodName, String requestBody) {
        UserTransactionsDTO dto = new UserTransactionsDTO();
        dto.setTransactionId(transactionId);
        dto.setClientIp(getIpAddress(request));
        dto.setServiceName(serviceName);
        dto.setMethodName(methodName);
        dto.setUserId(userId);
        if("GET".equals(request.getMethod())){
            dto.setRequest(getParameters(request));
        } else if("POST".equals(request.getMethod())){
            dto.setRequest(requestBody);
        }

        return userTransactionService.saveRequestLog(dto);
    }

    @Override
    public void logResponse(Long id, Long userId, String response) {
        userTransactionService.saveResponseLog(id,userId,response);
    }

    @Override
    public boolean isValidTransactionId(String transactionId, Long userId) {
        if(userTransactionService.isValidTransactionId(transactionId,userId)){
            LOGGER.info("UserTransactionHandlerImpl - isValidTransactionId - VALIDATED > transactionId: {} , userId: {} ", transactionId, userId);
            return true;
        }else{
            LOGGER.error("UserTransactionHandlerImpl ERROR - isValidTransactionId > transactionId: {} , userId: {} ", transactionId, userId);
            return false;
        }
    }

    private String getParameters(HttpServletRequest request) {
        StringBuffer posted = new StringBuffer();
        Enumeration<?> e = request.getParameterNames();
        if (e != null) {
            posted.append("?");
        }
        while (e.hasMoreElements()) {
            if (posted.length() > 1) {
                posted.append("&");
            }
            String curr = (String) e.nextElement();
            posted.append(curr + "=");
            if (curr.contains("password") || curr.contains("pass") || curr.contains("pwd")) { //TODO masked logging keys
                posted.append("*****");
            } else {
                posted.append(request.getParameter(curr));
            }
        }
        return posted.toString();
    }

    private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || (ipAddress != null && ipAddress.length() == 0)) {
            ipAddress = request.getRemoteAddr();
        }
        if (ipAddress.contains(",")) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }
        return ipAddress;
    }

    private String getHeaders(HttpServletRequest request) {
        StringBuffer posted = new StringBuffer();
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            multiValueMap.add(key, value);
        }

        multiValueMap.forEach((key, value) -> {
            posted.append(String.format("'%s' = %s", key, value.stream().collect(Collectors.joining("|"))));
            posted.append(System.lineSeparator());
        });

        return posted.toString();
    }

}