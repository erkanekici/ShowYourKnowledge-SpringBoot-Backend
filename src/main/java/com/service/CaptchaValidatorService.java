package com.service;

import com.dto.CaptchaResponse;
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

  public boolean validateCaptcha(String captcha){
    RestTemplate restTemplate = new RestTemplate();

    MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
    requestMap.add("secret", recaptchaSecret);
    requestMap.add("response", captcha);

    CaptchaResponse apiResponse = restTemplate.postForObject(GOOGLE_RECAPTCHA_ENDPOINT, requestMap, CaptchaResponse.class);

    if(apiResponse == null){
      return false;
    }

    LOGGER.info("CaptchaValidatorService - captchaResponse: {} , score: {}", apiResponse.getAction(), apiResponse.getScore());

    if(apiResponse.getErrorCodes() != null){
      LOGGER.info("CaptchaValidatorService ERROR: {}", Arrays.toString(apiResponse.getErrorCodes().toArray()));
//      if(Arrays.toString(apiResponse.getErrorCodes().toArray()).contains("timeout-or-duplicate")){
//        return true;
//      }
    }

    return Boolean.TRUE.equals(apiResponse.getSuccess()) && apiResponse.getScore().doubleValue() >= MIN_SCORE;
  }
}
