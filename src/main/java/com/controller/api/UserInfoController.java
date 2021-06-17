package com.controller.api;

import com.common.ErrorCodes;
import com.common.ObjectConversionUtil;
import com.common.RandomGenerator;
import com.controller.constants.ApiConstants;
import com.controller.constants.ApiTransactions;
import com.controller.handler.UserTransactionHandler;
import com.dto.CaptchaResult;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

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
            Long recordID = userTransactionsHandler.logRequest(request, transactionId, ApiConstants.DEFAULT_USERID, getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), sanitizedRequestBody);

            JSONObject requestObject = null;
            try {
                requestObject = new JSONObject(sanitizedRequestBody);

                //Captcha Validation
                CaptchaResult captchaResult = captchaService.isCaptchaValidated(transactionId, requestObject);
                if(!captchaResult.isValidated()){
                    //logResponse
                    userTransactionsHandler.logFailedResponse(recordID, ApiConstants.DEFAULT_USERID, ApiTransactions.error(captchaResult.getErrCode()).toString(),captchaResult.getErrCode(),captchaResult.getErrMessage());
                    ResponseEntity.ok().body(ApiTransactions.error(captchaResult.getErrCode())); // TODO test
                }

                //TODO isEmailExist method

                // UserInfoDTO requestObject = ObjectConversionUtil.getInstance().getObjectByJsonString(sanitizedRequestBody, new TypeReference<UserInfoDTO>() {});
                UserInfoDTO userInfo = new UserInfoDTO();
                userInfo.setEmail(requestObject.getString("email"));
                //TODO password hash - userInfo.setPassword(passwordEncoder.encode(accountDto.getPassword()));
                userInfo.setPassword(requestObject.getString("password"));

                UserInfoDTO userInfoDTO = userService.register(userInfo);

                //TODO ASYNC EXAMPLE
//                CompletableFuture<UserInfoDTO> result = CompletableFuture.supplyAsync(() -> userService.register(userInfo), Executors.newCachedThreadPool());
//                userInfoDTO = result.get();

                if (userInfoDTO != null) {
                    //logResponse
                    userTransactionsHandler.logSuccessfulResponse(recordID, userInfoDTO.getId(), userInfoDTO.toString());
                    return ResponseEntity.ok().body(ApiTransactions.success(userInfoDTO)); // TODO test
                } else {
                    //logResponse
                    userTransactionsHandler.logFailedResponse(recordID, ApiConstants.DEFAULT_USERID,ApiTransactions.error(ErrorCodes.ERR_6.name()).toString(), ErrorCodes.ERR_6.name(),ErrorCodes.ERR_6.getDescription());
                    return ResponseEntity.ok().body(ApiTransactions.error(ErrorCodes.ERR_6.name())); // TODO test
                }
            } catch (Exception e) {
                LOGGER.error("UserInfoController ERROR - register > Request: {} , Exception: {} ", sanitizedRequestBody, e.getMessage());
                //logResponse
                userTransactionsHandler.logFailedResponse(recordID, ApiConstants.DEFAULT_USERID, ApiTransactions.error(ErrorCodes.ERR_6.name()).toString(), ErrorCodes.ERR_6.name(),ErrorCodes.ERR_6.getDescription());

                //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR: " + e.getMessage());
                return ResponseEntity.ok().body(ApiTransactions.error(ErrorCodes.ERR_6.name())); // TODO test
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
            Long recordID = userTransactionsHandler.logRequest(request, transactionId, ApiConstants.DEFAULT_USERID, getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), sanitizedRequestBody);

            JSONObject requestObject = null;
            try {
                requestObject = new JSONObject(sanitizedRequestBody);

                //Captcha Validation
                CaptchaResult captchaResult = captchaService.isCaptchaValidated(transactionId, requestObject);
                if(!captchaResult.isValidated()){
                    //logResponse
                    userTransactionsHandler.logFailedResponse(recordID, ApiConstants.DEFAULT_USERID, ApiTransactions.error(captchaResult.getErrCode()).toString(),captchaResult.getErrCode(),captchaResult.getErrMessage());
                    ResponseEntity.ok().body(ApiTransactions.error(captchaResult.getErrCode())); // TODO test
                }

                UserInfoDTO userInfoDTO = userService.getUserInfoByEmailAndPassword(requestObject.getString("email"), requestObject.getString("password"));

                //TODO aktiflik kontrolü

                if(userInfoDTO == null){
                    //logResponse
                    userTransactionsHandler.logFailedResponse(recordID, ApiConstants.DEFAULT_USERID,ApiTransactions.error(ErrorCodes.ERR_3.name()).toString(), ErrorCodes.ERR_3.name(), ErrorCodes.ERR_3.getDescription());
                    return ResponseEntity.ok().body(ApiTransactions.error(ErrorCodes.ERR_3.name())); // TODO test
                }

                //TODO Login de hata olmazsa önyüze transactionId iletilir ve sonraki işlemlerde userID ile beraber bu transactionId beklenir.
                // TransactionId sonraki her işlemde validasyondan geçer.
                //logResponse
                userTransactionsHandler.logSuccessfulResponse(recordID, userInfoDTO.getId(), userInfoDTO.toString());
                return ResponseEntity.ok().body(ApiTransactions.success(userInfoDTO)); // TODO test
            } catch (Exception e) {
                LOGGER.error("UserInfoController ERROR - login > Request: {} , Exception: {} ", sanitizedRequestBody, e.getMessage());
                //logResponse
                userTransactionsHandler.logFailedResponse(recordID, ApiConstants.DEFAULT_USERID, ApiTransactions.error(ErrorCodes.ERR_7.name()).toString(), ErrorCodes.ERR_7.name(),ErrorCodes.ERR_7.getDescription());
                return ResponseEntity.ok().body(ApiTransactions.error(ErrorCodes.ERR_7.name())); // TODO test TODO generic
            }
        } catch (Exception ex){
            //TODO logRequest sırasında hata alinirsa calisir
            LOGGER.error("UserInfoController ERROR - login > Request: {} , Exception: {} ", requestBody, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiTransactions.error("Giriş Başarısız")); // TODO test
        }
    }
}
