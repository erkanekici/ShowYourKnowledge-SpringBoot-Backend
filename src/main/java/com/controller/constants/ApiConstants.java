package com.controller.constants;

import org.springframework.http.MediaType;

public final class ApiConstants {

    public static final String RESPONSE_SUCCESS = "Success";
    public static final String RESPONSE_FAILED = "Failed";

    public static final String CONTENT_TYPE_APPLICATION_JSON_VALUE_WITH_UTF8 = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8";
    public static final String CONTENT_TYPE_ALL = MediaType.ALL_VALUE;

    public final class ApiGroups {
        public final class UserApi {
            public static final String NAME = "user-api";
            public static final String TITLE = "UserApi";
            public static final String PATH = "/serviceCaller/**";
            public static final String DESCRIPTION = "User Api";
        }
        public final class DocumentApi {
            public static final String NAME = "document-api";
            public static final String TITLE = "DocumentApi";
            public static final String PATH = "/api/v1/document/**";
            public static final String DESCRIPTION = "Document Api";
        }
    }
}
