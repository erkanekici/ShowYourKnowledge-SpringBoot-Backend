package com.controller;

public class CaptchaResultHolder {
  boolean isValidated;
  String message;

  CaptchaResultHolder(boolean isValidated, String message){
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
