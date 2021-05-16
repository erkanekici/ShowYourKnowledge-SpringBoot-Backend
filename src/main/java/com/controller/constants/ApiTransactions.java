package com.controller.constants;

import com.common.ErrorCodes;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.util.List;

public final class ApiTransactions {

    public static final String sanitizeRequest(String request){
        return Jsoup.clean(request, Whitelist.basic());
    }

    public static final JSONObject success(Object result) {
        JSONObject obj = new JSONObject();
        obj.accumulate("resultCode", ApiConstants.SUCCESSFUL_CODE);
        obj.accumulate("result", result);
        return obj;
    }

    public static final JSONObject error(String errorCode) {
        JSONObject obj = new JSONObject();
        obj.accumulate("resultCode", ApiConstants.FAILED_CODE);
        obj.accumulate("errorCode", errorCode);
        return obj;
    }

    public static final JSONObject unknownError() {
        JSONObject obj = new JSONObject();
        obj.accumulate("resultCode", ApiConstants.FAILED_CODE);
        obj.accumulate("errorCode", ErrorCodes.ERR_1.name());
        return obj;
    }


    public static final JSONObject errorList(List<ErrorCodes> errorCodesList){
        JSONObject obj = new JSONObject();
        obj.accumulate("resultCode", ApiConstants.FAILED_CODE);

        JSONArray errors = new JSONArray();
        for(ErrorCodes errCode : errorCodesList){
            JSONObject err = new JSONObject();
            err.accumulate("errorCode", errCode.name());
            errors.put(err);
        }

        obj.accumulate("errors",errors);

        return obj;
    }


}
