package com.connector;

import com.config.EnvironmentConfig;
import com.util.MaskingUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RemoteServiceCallerImpl implements RemoteServiceCaller {

    private static final Logger logger = LoggerFactory.getLogger(RemoteServiceCallerImpl.class);

    @Autowired
    MaskingUtil maskingUtil;

    @Override
    public JSONObject sendData(JSONObject jsonObject, String serviceName, String methodName){

        JSONObject jsonResult = null;
        String incomingRequest = jsonObject.toString();
        String receivedMessage = "";

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Accept", MediaType.APPLICATION_XML_VALUE);
            if("prod".equals(EnvironmentConfig.profile)){
               // headers.add("username", EnvironmentConfig.esbUsername);
               // headers.add("password", EnvironmentConfig.esbPassword);
            }
            else if("test".equals(EnvironmentConfig.profile) || "dev".equals(EnvironmentConfig.profile)){
                if("OnlineStoreCreditService".equals(serviceName)){
                    headers.add("username", "vuser");
                    headers.add("password", "test");
                }
                else if("INSTANTCreditService".equals(serviceName)){
                    headers.add("username", "testuser");
                    headers.add("password", "test");
                }
            }

            HttpEntity<String> requestBody = new HttpEntity<>(incomingRequest, headers);
            String urlWithParams = //EnvironmentConfig.esbRestAddress+
                    "/" + serviceName + "/" + methodName;

            logger.info("ESB START URL: " + urlWithParams);
            logger.info("REQUEST: " + maskingUtil.maskRequest(jsonObject,methodName));

            //Sending Message
            receivedMessage = restTemplate.postForObject(urlWithParams, requestBody, String.class);
            jsonResult = new JSONObject(receivedMessage);

        } catch (Exception e) {
            logger.error("ESB connection error. REQUEST: " + maskingUtil.maskRequest(jsonObject,methodName));
            logger.error("ESB connection error. RESPONSE: " + receivedMessage);
            throw e;
        }

        return jsonResult;
    }

    //HttpURLConnection icin timeout ekleme.
    //Bu islem alternatif olarak jvm vm parametresi olarak soyle gecilebilir:
    //-Dsun.net.client.defaultConnectTimeout=15000
    //-Dsun.net.client.defaultReadTimeout=10000
    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(15000);
        clientHttpRequestFactory.setReadTimeout(10000);
        return clientHttpRequestFactory;
    }
}