package com.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CaptchaResponse {

  private Boolean success;
  private Number score;
  private String action;
  private Date challenge_ts;
  private String hostname;
  @JsonProperty("error-codes")
  private List<String> errorCodes;

}

