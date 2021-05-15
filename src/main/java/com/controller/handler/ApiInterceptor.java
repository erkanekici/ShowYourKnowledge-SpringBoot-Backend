package com.controller.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.stream.Collectors;

public class ApiInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOGGER.info(
                "API CALL - " + request.getHeader("transactionId") + System.lineSeparator() + //TODO header veya body den alinacak
                "URI: " + request.getRequestURI() + System.lineSeparator() +
                "PARAMETERS: " + getParameters(request) + System.lineSeparator() +
                "TYPE: " + request.getMethod() + System.lineSeparator() +
                "IP: " + getIpAddress(request)  + System.lineSeparator() +
                "HEADERS: " + System.lineSeparator() + getHeaders(request)
        );
        return true;
    }

//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//
//    }

//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        if (ex != null){
//            ex.printStackTrace();
//        }
//        LOGGER.info("[afterCompletion][" + request + "][exception: " + ex + "]");
//    }

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