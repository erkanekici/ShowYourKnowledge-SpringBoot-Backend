package com.common;

public enum ErrorCodes {

    ERR_1("Bilinmeyen bir hata oluştu"),
    ERR_2("Gelen istekte captcha değeri bulunamadı"),
    ERR_3("Girdiğiniz e-posta ve şifre bilgisine ait kullanıcı bulunmamaktadır. Lütfen bilgilerinizi kontrol ediniz."),
    ERR_4("Captcha null"),
    ERR_5("Captcha geçersiz"),
    ERR_6("Kayıt esnasında bir hata oluştu"),
    ERR_7("Giriş esnasında bir hata oluştu"),
    ERR_8("Hata"),
    ERR_9("Hata"),
    ERR_10("Hata");

    private String description;
    public String getDescription() {
        return description;
    }

    ErrorCodes(String description) {
        this.description = description;
    }

//    public static EnumSet<ErrorCodes> ApiErrors = EnumSet.of(ERR_1,ERR_2);

//    public static boolean isApiErrorCode(String code) {
//        for (ErrorCodes errCode : values()) {
//            if (errCode.name().equals(code)) {
//                return errCode.isApiError(errCode);
//            }
//        }
//        return false;
//    }

//    public boolean isApiError(ErrorCodes errCode) {
//        return ErrorCodes.ApiErrors.contains(errCode);
//    }
}
