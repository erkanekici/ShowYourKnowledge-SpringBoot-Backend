package com.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;

public class CaptchaResponse {

  private Boolean success;
  private Number score;
  private String action;
  private Date challenge_ts;
  private String hostname;
  @JsonProperty("error-codes")
  private List<String> errorCodes;

  public Boolean getSuccess() { return success; }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public Date getChallenge_ts() {
    return challenge_ts;
  }

  public void setChallenge_ts(Date challenge_ts) {
    this.challenge_ts = challenge_ts;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public List<String> getErrorCodes() {
    return errorCodes;
  }

  public void setErrorCodes(List<String> errorCodes) {
    this.errorCodes = errorCodes;
  }

  public Number getScore() { return score; }

  public void setScore(Number score) { this.score = score; }

  public String getAction() { return action; }

  public void setAction(String action) { this.action = action; }
}

