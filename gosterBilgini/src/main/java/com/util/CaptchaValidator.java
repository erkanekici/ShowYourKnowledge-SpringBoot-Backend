package com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class CaptchaValidator {

  private static final String GOOGLE_RECAPTCHA_ENDPOINT = "https://www.google.com/recaptcha/api/siteverify";
  private static final Logger logger = LoggerFactory.getLogger(CaptchaValidator.class);

  @Value("${google.recaptcha.secret}")
  private String recaptchaSecret;

  public boolean validateCaptcha(String captchaResponse){
    RestTemplate restTemplate = new RestTemplate();

    MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
    requestMap.add("secret", recaptchaSecret);
    requestMap.add("response", captchaResponse);

    CaptchaResponse apiResponse = restTemplate.postForObject(GOOGLE_RECAPTCHA_ENDPOINT, requestMap, CaptchaResponse.class);

    if(apiResponse == null){
      return false;
    }

    logger.info("Captcha Response: " + apiResponse.getAction() + " " + apiResponse.getScore());
    if(apiResponse.getErrorCodes() != null){
      logger.info("Captcha Errors: " + Arrays.toString(apiResponse.getErrorCodes().toArray()));
      if(Arrays.toString(apiResponse.getErrorCodes().toArray()).contains("timeout-or-duplicate")){
        return true;
      }
    }

    return Boolean.TRUE.equals(apiResponse.getSuccess()) && apiResponse.getScore().doubleValue() >= 0.5;
  }
}
