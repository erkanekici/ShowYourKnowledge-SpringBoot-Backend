package com.dto;

public class CaptchaResult {

    private boolean isValidated;
    private String errCode;
    private String errMessage;

    public CaptchaResult(boolean isValidated, String errCode, String errMessage){
        this.isValidated = isValidated;
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public boolean isValidated() {
    return isValidated;
  }

    public String getErrCode() { return errCode; }

    public String getErrMessage() { return errMessage; }
}
