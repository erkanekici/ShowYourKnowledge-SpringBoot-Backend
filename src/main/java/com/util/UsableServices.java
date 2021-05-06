package com.util;

import java.util.Arrays;
import java.util.Optional;

public enum UsableServices {

    GET_USER("UserService","getUser"),
    GET_TOPIC("TopicService","getTopic");

    private String serviceName;
    private String methodName;

    UsableServices(String serviceName, String methodName){
        this.serviceName = serviceName;
        this.methodName= methodName;
    }

    public static boolean anyMatch(String serviceName,String methodName) {
        return Arrays.stream(UsableServices.values())
                .anyMatch(x->x.getServiceName().equals(serviceName) && x.getMethodName().equals(methodName));
    }

   public static Optional<UsableServices> getUsableServices(String methodName,String serviceName) {
        return Arrays.stream(UsableServices.values())
                .filter(x-> x.getMethodName().equals(methodName) && x.getServiceName().equals(serviceName))
                .findAny();
    }

    public String getServiceName() {
        return serviceName;
    }
    public String getMethodName() {
        return methodName;
    }

}