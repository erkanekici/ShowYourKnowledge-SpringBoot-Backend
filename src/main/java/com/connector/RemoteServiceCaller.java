package com.connector;

import org.json.JSONObject;

public interface RemoteServiceCaller {
    JSONObject sendData(JSONObject jsonObject, String serviceName, String methodName);
}