package com.controller.api;

import com.common.ObjectConversionUtil;
import com.common.RandomGenerator;
import com.controller.constants.ApiConstants;
import com.controller.constants.ApiTransactions;
import com.controller.handler.UserTransactionHandler;
import com.dto.UserInfoDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.service.CaptchaValidatorService;
import com.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(
        path = "/api/v1/user",
        produces = {ApiConstants.CONTENT_TYPE_APPLICATION_JSON_VALUE_WITH_UTF8},
        consumes = {ApiConstants.CONTENT_TYPE_ALL,ApiConstants.CONTENT_TYPE_APPLICATION_JSON_VALUE_WITH_UTF8}
        )
@ResponseBody
@Api(value = ApiConstants.ApiGroups.UserInfoApi.TITLE)
public class UserInfoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoController.class);
    //private final Object lock = new Object(); TODO

    @Autowired
    UserService userService;
    @Autowired
    UserTransactionHandler userTransactionsHandler;
    @Autowired
    CaptchaValidatorService captchaService;

    @PostMapping(value = "/register")
    @ApiOperation(value = "", notes = "Register Service")
    public ResponseEntity register(HttpServletRequest request, @RequestBody String requestBody){
        try {
            String sanitizedRequestBody = ApiTransactions.sanitizeRequest(requestBody);
            String transactionId = RandomGenerator.getInstance().generateUUID() + RandomGenerator.getInstance().generateRandom();

            //logRequest
            Long recordID = userTransactionsHandler.logRequest(request, transactionId, 0L, getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), sanitizedRequestBody);

            try {
                UserInfoDTO requestObject = ObjectConversionUtil.getInstance().getObjectByJsonString(sanitizedRequestBody, new TypeReference<UserInfoDTO>() {});

                // TODO Capctha validation

                UserInfoDTO userInfoDTO = userService.register(requestObject);
                if (userInfoDTO != null) {
                    //logResponse
                    userTransactionsHandler.logResponse(recordID, userInfoDTO.getId(), userInfoDTO.toString());
                    return ResponseEntity.ok().body(ApiTransactions.success(userInfoDTO)); // TODO test
                } else {
                    //logResponse
                    userTransactionsHandler.logResponse(recordID, 0L, ApiConstants.RESPONSE_FAILED);
                    return ResponseEntity.ok().body(ApiTransactions.error("Kayıt gerçekleştirilemedi")); // TODO test
                }
            } catch (Exception e) {
                LOGGER.error("UserInfoController ERROR - register > Request: {} , Exception: {} ", sanitizedRequestBody, e.getMessage());
                //logResponse
                userTransactionsHandler.logResponse(recordID, 0L, ApiConstants.RESPONSE_FAILED);

                //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR: " + e.getMessage());
                return ResponseEntity.ok().body(ApiTransactions.error("Kayıt gerçekleştirilemedi")); // TODO test
            }
        } catch (Exception ex){
            LOGGER.error("UserInfoController ERROR - register > Request: {} , Exception: {} ", requestBody, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiTransactions.error("Kayıt gerçekleştirilemedi")); // TODO test
        }
    }

    @PostMapping(value = "/login")
    @ApiOperation(value = "", notes = "Login Service")
    public ResponseEntity login(HttpServletRequest request, @RequestBody String requestBody){
        String sanitizedRequestBody = ApiTransactions.sanitizeRequest(requestBody);
        String transactionId = RandomGenerator.getInstance().generateUUID() + RandomGenerator.getInstance().generateRandom();
        try {
            //logRequest
            Long recordID = userTransactionsHandler.logRequest(request, transactionId, 0L, getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), sanitizedRequestBody);

            JSONObject requestObject = null;
            try {
                requestObject = new JSONObject(sanitizedRequestBody);


                //TODO Login esnasında bir hata olursa önyüze transactionId iletilmez ve userTransactions.logResponse a önyüze dönülen login Error kaydı atılır
                // Login de hata olmazsa önyüze transactionId iletilir ve sonraki işlemlerde userID ile beraber bu transactionId de beklenir.
                // TransactionId sonraki her işlemde validasyondan geçer.

                //TODO Capctha validation

                UserInfoDTO userInfoDTO = userService.getUserInfoByEmailAndPassword(requestObject.getString("email"), requestObject.getString("password"));

                //logResponse
                userTransactionsHandler.logResponse(recordID, userInfoDTO.getId(), userInfoDTO.toString());

                return ResponseEntity.ok().body(ApiTransactions.success(userInfoDTO)); // TODO test
            } catch (Exception e) {
                LOGGER.error("UserInfoController ERROR - login > Request: {} , Exception: {} ", sanitizedRequestBody, e.getMessage());
                //logResponse
                userTransactionsHandler.logResponse(recordID, 0L, ApiConstants.RESPONSE_FAILED);
                return ResponseEntity.ok().body(ApiTransactions.error("Giriş Başarısız")); // TODO test TODO generic
            }
        } catch (Exception ex){
            LOGGER.error("UserInfoController ERROR - login > Request: {} , Exception: {} ", requestBody, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiTransactions.error("Giriş Başarısız")); // TODO test
        }
    }
}
