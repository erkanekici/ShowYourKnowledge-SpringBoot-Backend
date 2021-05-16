package com.controller.constants;

import org.springframework.http.MediaType;

public final class ApiConstants {

    public static final Long DEFAULT_USERID = 0L;

    public static final int SUCCESSFUL_CODE = 1;
    public static final int FAILED_CODE = 0;

    public static final String CONTENT_TYPE_APPLICATION_JSON_VALUE_WITH_UTF8 = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8";
    public static final String CONTENT_TYPE_ALL = MediaType.ALL_VALUE;

    public static final class ApiGroups {
        public static final class UserInfoApi {
            public static final String NAME = "user-api";
            public static final String TITLE = "UserInfoService";
            public static final String PATH = "/api/v1/user/**";
            public static final String DESCRIPTION = "User Api";
        }
//        public static final class DocumentApi {
//            public static final String NAME = "document-api";
//            public static final String TITLE = "DocumentApi";
//            public static final String PATH = "/api/v1/document/**";
//            public static final String DESCRIPTION = "Document Api";
//        }
    }

}
