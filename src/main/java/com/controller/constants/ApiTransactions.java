package com.controller.constants;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public final class ApiTransactions {

    public static final String sanitizeRequest(String request){
        return Jsoup.clean(request, Whitelist.basic());
    }

    public static final JSONObject unknownError() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("resultCode", ApiConstants.ERROR_CODE);
        jsonObject.accumulate("errorMessage", ApiConstants.UNKNOWN_ERROR);
        return jsonObject;
    }

    public static final JSONObject error(String errorMessage) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("resultCode", ApiConstants.ERROR_CODE);
        jsonObject.accumulate("errorMessage", errorMessage);
        return jsonObject;
    }

    public static final JSONObject success(Object result) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("resultCode", ApiConstants.SUCCESSFUL_CODE);
        jsonObject.accumulate("result", result);
        return jsonObject;
    }

}
