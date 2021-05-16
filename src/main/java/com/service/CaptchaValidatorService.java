package com.service;

import com.common.ErrorCodes;
import com.dto.CaptchaResponse;
import com.dto.CaptchaResult;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class CaptchaValidatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaValidatorService.class);

    private static final String GOOGLE_RECAPTCHA_ENDPOINT = "https://www.google.com/recaptcha/api/siteverify";
    private static final double MIN_SCORE = 0.5;

    @Value("${google.recaptcha.secret}")
    private String recaptchaSecret;

    public CaptchaResult isCaptchaValidated(String transactionId, JSONObject request) {
        if (request.has("captcha")) {
            try {
                if (JSONObject.NULL.equals(request.get("captcha"))) {
                    LOGGER.error("CaptchaValidatorService ERROR - isCaptchaValidated > Request: {} , Exception: {} ", request.toString(), ErrorCodes.ERR_4.getDescription());
                    return new CaptchaResult(false, ErrorCodes.ERR_4.name(), ErrorCodes.ERR_4.getDescription());
                }

                boolean isValidCaptcha = validateCaptcha(transactionId, request.getString("captcha"));
                if (!isValidCaptcha) {
                    LOGGER.error("CaptchaValidatorService ERROR - isCaptchaValidated > Request: {} , Exception: {} ", request.toString(), ErrorCodes.ERR_5.getDescription());
                    return new CaptchaResult(false, ErrorCodes.ERR_5.name(), ErrorCodes.ERR_5.getDescription());
                } else {
                    return new CaptchaResult(true, null, null);
                }
            } catch (Exception e) {
                LOGGER.error("CaptchaValidatorService ERROR - isCaptchaValidated > Request: {} , Exception:", request.toString());
                e.printStackTrace();
                return new CaptchaResult(false, ErrorCodes.ERR_1.name(), ErrorCodes.ERR_1.getDescription() );
            }
        } else {
            LOGGER.error("CaptchaValidatorService ERROR - isCaptchaValidated > Request: {} , Exception: {} ", request.toString(), ErrorCodes.ERR_2.getDescription());
            return new CaptchaResult(false, ErrorCodes.ERR_2.name(), ErrorCodes.ERR_2.getDescription());
        }
    }

    private boolean validateCaptcha(String transactionId, String captcha) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("secret", recaptchaSecret);
        requestMap.add("response", captcha);

        CaptchaResponse apiResponse = restTemplate.postForObject(GOOGLE_RECAPTCHA_ENDPOINT, requestMap, CaptchaResponse.class);

        if (apiResponse == null) {
            return false;
        }

        LOGGER.info("CaptchaValidatorService - validateCaptcha - transactionId: {}, response: {} , score: {}", transactionId, apiResponse.getAction(), apiResponse.getScore());

        if (apiResponse.getErrorCodes() != null) {
            LOGGER.error("CaptchaValidatorService ERROR - validateCaptcha - Error: {}", Arrays.toString(apiResponse.getErrorCodes().toArray()));
            // if(Arrays.toString(apiResponse.getErrorCodes().toArray()).contains("timeout-or-duplicate")){
            //   return true;
            // }
        }

        return Boolean.TRUE.equals(apiResponse.getSuccess()) && apiResponse.getScore().doubleValue() >= MIN_SCORE;
    }

}
