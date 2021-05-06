package com.util;

import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Service
public final class GetHTTPRequestParams {

    public HashMap getIPandUserAgent(HttpServletRequest request) {

        //Client IP
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        if (ipAddress.contains(",")) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }

        //UserAgent
        String userAgent = request.getHeader("User-Agent");

        HashMap<String, String> requestInfo = new HashMap<>();
        requestInfo.put("IP",ipAddress);
        requestInfo.put("USERAGENT",userAgent);
        return requestInfo;
    }

}