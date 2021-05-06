package com.util;

import com.model.UsableServices;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public final class MaskingUtil {

    public JSONObject maskRequest(JSONObject request,String methodName) throws JSONException {
        JSONObject maskedRequest = new JSONObject(request.toString());
        if(UsableServices.GET_USER.getMethodName().equals(methodName)){
            if(request.has("motherMaidenName")) {
                maskedRequest.put("motherMaidenName","Masked");
            }
        }
        else if(UsableServices.GET_USER.getMethodName().equals(methodName)){
            if(request.has("secretKey")) {
                maskedRequest.put("secretKey","Masked");
            }
            if(request.has("questionAnswerTable")) {
                maskedRequest.put("questionAnswerTable","Masked");
            }
        }
        else if(UsableServices.GET_USER.getMethodName().equals(methodName)){
            if(request.has("answer")) {
                maskedRequest.put("answer","Masked");
            }
        }

        return maskedRequest;
    }

}