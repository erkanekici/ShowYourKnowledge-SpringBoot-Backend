package com.controller.model;

public class CaptchaResult {

  boolean isValidated;
  String message;

  public CaptchaResult(boolean isValidated, String message){
    this.isValidated=isValidated;
    this.message=message;
  }

  public boolean isValidated() {
    return isValidated;
  }

  public String getMessage() {
    return message;
  }
}
