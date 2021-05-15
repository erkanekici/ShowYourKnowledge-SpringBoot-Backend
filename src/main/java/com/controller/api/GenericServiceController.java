//package com.controller.api;
//
//import com.config.EnvironmentConfig;
//import com.controller.constants.ApiConstants;
//import com.dto.CaptchaResult;
//import com.controller.handler.UsableServices;
//import com.dto.UserInfoDTO;
//import com.entity.Topic;
//import com.service.CaptchaValidatorService;
//import com.common.*;
//import com.service.UserService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.apache.commons.io.IOUtils;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.jsoup.Jsoup;
//import org.jsoup.safety.Whitelist;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.time.Duration;
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//
//@RestController
//@CrossOrigin(value = "*")
//@RequestMapping(path = "/serviceCaller",
////        method = RequestMethod.POST,
//        produces = {ApiConstants.CONTENT_TYPE_APPLICATION_JSON_VALUE_WITH_UTF8},
//        consumes = {ApiConstants.CONTENT_TYPE_ALL,ApiConstants.CONTENT_TYPE_APPLICATION_JSON_VALUE_WITH_UTF8})
//@ResponseBody
//@Api(value = "GenericService")
//public class GenericServiceController {
//
//    private final Object lock = new Object();
//    private static final Logger logger = LoggerFactory.getLogger(GenericServiceController.class);
//
//    @Autowired
//    UserService userService;
//    @Autowired
//    private CaptchaValidatorService captchaService;
//    @Autowired
//    MailUtil mailUtil;
//    @Autowired
//    GetHTTPRequestParams getHTTPRequestParams;
//    @Autowired
//    MaskingUtil maskingUtil;
//
//    @PostMapping(value = "/{serviceName}/{methodName}")
//    @ApiOperation(value = "", notes = "Generic service call")
//    public String serviceListener(
//                HttpServletRequest request,
//                @PathVariable("serviceName") String serviceName,
//                @PathVariable("methodName") String methodName,
//                @RequestBody String serviceBagJSON
//                //@RequestHeader(value = "username") String username,
//                //@RequestHeader(value = "password") String password
//            ) throws JSONException
//    {
//        //cross-site scripting (XSS) icin request temizleme
//        String sanitizedServiceBagJSON = Jsoup.clean(serviceBagJSON, Whitelist.basic());
//
//        logger.info("YENI ISTEK => " + serviceName + "/" + methodName + " => " + serviceBagJSON);
//
//        // TRANSACTION ID KONTROLU LAZIM (Transactions veya activitiy tablosundan)
//        // 1-Formati
//        // 2-Süresi
//
//        //IP ve UserAgent Loglama
//        HashMap requestInfo = getHTTPRequestParams.getIPandUserAgent(request);
//        logger.info("Credit - IP: " + requestInfo.get("IP"));
//        logger.info("Credit - UserAgent: " + requestInfo.get("USERAGENT"));
//
//        //request isleniyor
//        JSONObject serviceResponse = null;
//        JSONObject serviceRequest = null;
//        try {
//            serviceRequest = new JSONObject(sanitizedServiceBagJSON);
//            //Request Maskeleme
//            JSONObject maskedRequest = maskingUtil.maskRequest(serviceRequest,methodName);
//            logger.info("Credit - First Request: " + serviceName + "/" + methodName + " => " + maskedRequest.toString());
//            try {  // TRANSACTION ID KONTROLU LAZIM (Transactions veya activitiy tablosundan)
//                //Guvenlik icin transactionId kontrolu
//                if(!isTransactionIdUsable(serviceRequest)){
//                    logger.error("Credit - Wrong TransactionId: " + maskedRequest.toString());
//                    //return spesificError("Hatalı işlem kodu");
//                }
//
//                //Islem timeout kontrolu
//                if(isTransactionTimeUp(serviceRequest)){
//                    logger.error("Credit - Expired TransactionId: " + maskedRequest.toString());
//                    //insertTimeoutLog(serviceRequest.getString("transactionId"),"",serviceName,methodName,Instant.now());
//                    //return expiredTransactionErrorForN11();
//                }
//
//                //GetOTP cagrim sayisi kontrolu
//                if(UsableServices.GET_USER.getMethodName().equals(methodName)) {
//                    try {
//                        List<Map<String, Object>> result = dbUtil.getNumberOfValidateOTPServiceCalls(serviceRequest.getString("transactionId"),methodName,"ads");
//                        if (result != null && result.size() > 4) {
//                            //return maxServiceCallError("Maksimum SMS gönderim isteği sayısı aşıldı");
//                        }
//                    } catch (Exception e) {
//                        logger.error("Credit - GetOTPCallCheck - DB Connection or DB Dataset error: " + maskedRequest.toString() + " Error Detail: ");
//                        e.printStackTrace();
//                        //return standardUnknownError();
//                    }
//                }
//
//                //OBJECT MAPPER
////                CompayProcessParameters compayParams = new ObjectMapper().readValue(serviceRequest.toString(), CompayProcessParameters.class);
////                dbTransactions.saveTransactionParameters(compayParams,getLocalDateTime(Instant.now()));
//
//            } catch (Exception e1) {
//                logger.error("Credit - First Check Error: " + maskedRequest.toString() + " Error Detail: ");
//                e1.printStackTrace();
//                //return standardUnknownError();
//            }
//        } catch (JSONException e1) {
//            logger.error("JSON parse error: " + sanitizedServiceBagJSON);
//            e1.printStackTrace();
//            return unknownError();
//        }
//
//        try {
//            CaptchaResult captchaHolder = isCaptchaValidated(methodName, serviceRequest);
//            if(!captchaHolder.isValidated()){
//                //return captchaNotValidJSON(captchaHolder.getMessage());
//            }
//            if("Login".equals(methodName)){
//                UserInfoDTO userInfoDTO = userService.getUserInfoByEmailAndPassword(serviceRequest.getString("email"), serviceRequest.getString("password"));
//                if(userInfoDTO == null){
//                    logger.error("Login Error: " + serviceRequest.toString());
//                    return classicError("Girdiğiniz e-posta ve şifre bilgisine ait kullanıcı bulunmamaktadır. Lütfen bilgilerinizi kontrol ediniz.");
//                }
////                if(userInfo.size() > 1){
////                    //OLMAMASI GEREKEN BİR DURUM
////                    logger.error("Login Error: " + serviceRequest.toString());
////                    return classicError("Teknik bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.");
////                }
//                if(userInfoDTO.getActivity() != 1){
//                    //BURADA Aktivasyon maili gönder
//                    return classicError("Hesabınız henüz aktif edilmemiştir. Kayıtlı e-posta adresinize yeni aktivasyon maili gönderilmiştir.");
//                }
//                //LOGİN BAŞARILI userinfo bilgilerini dön
//            }
//            if("Register".equals(methodName)){
//                String id = dbUtil.findUserIdByEmail(serviceRequest.getString("email"));
//                if(id != null){
//                    logger.error("Register Error: " + serviceRequest.toString());
//                    return classicError("Girdiğiniz e-posta adresine kayıtlı kullanıcı bulunmaktadır.");
//                }
//                Instant start = Instant.now();
//                dbUtil.insertUser(
//                        serviceRequest.getString("transactionId"),
//                        serviceRequest.getString("email"),
//                        serviceRequest.getString("password"),
//                        "Yeni Kullanıcı",
//                        0,
//                        0,
//                        getLocalDateTime(start));
//
//                mailUtil.sendActivationMail(serviceRequest.getString("email"),serviceRequest.getString("transactionId"));
//            }
//            if("GetTopic".equals(methodName)){
//                synchronized (lock) {
//                    try{
//                        List<Topic> topics = dbUtil.getSuitableTopics();
//                        if(topics.size() == 0){
//                            String topicId = UUID.randomUUID().toString();
//                            dbUtil.insertTopic(topicId,serviceRequest.getString("transactionId"));
//
//                            JSONObject jsonObject=new JSONObject();
//                            jsonObject.accumulate("resultCode",1);
//                            jsonObject.accumulate("gameStatus",0);
//                            jsonObject.accumulate("topicId",topicId);
//                            jsonObject.accumulate("topic","/topic/chat.addUser/"+topicId);
//                            //jsonObject.accumulate("topic","/topic/chat.addUser/room1");
//                            return jsonObject.toString();
//                        }else{
//                            dbUtil.updateTopicById(topics.get(0).getId(),serviceRequest.getString("transactionId"));
//
//                            JSONObject jsonObject=new JSONObject();
//                            jsonObject.accumulate("resultCode",1);
//                            jsonObject.accumulate("gameStatus",1);
//                            jsonObject.accumulate("topicId",topics.get(0).getId());
//                            jsonObject.accumulate("topic","/topic/chat.addUser/"+topics.get(0).getId());
//                            //jsonObject.accumulate("topic","/topic/chat.addUser/room1");
//                            return jsonObject.toString();
//                        }
//                    } catch (Exception e) {
//                        logger.error("GetTopic Error: " + serviceRequest.toString());
//                        e.printStackTrace();
//                        return unknownError();
//                    }
//                }
//            }
//            if("GetGameStatus".equals(methodName)){
//                List<Topic> topic = dbUtil.getTopicById(serviceRequest.getString("topicId"));
//                if(topic.size() == 0){
//                    logger.error("GetGameStatus Error: " + serviceRequest.toString());
//                    return classicError("Teknik bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.");
//                }
//                if(topic.size() > 1){
//                    //OLMAMASI GEREKEN BİR DURUM
//                    logger.error("GetGameStatus Error: " + serviceRequest.toString());
//                    return classicError("Teknik bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.");
//                }
//                if(topic.get(0).getUserid2() != null){
//                    JSONObject jsonObject=new JSONObject();
//                    jsonObject.accumulate("resultCode",1);
//                    jsonObject.accumulate("gameStatus",1);
//                    return jsonObject.toString();
//                }else{
//                    JSONObject jsonObject=new JSONObject();
//                    jsonObject.accumulate("resultCode",1);
//                    jsonObject.accumulate("gameStatus",0);
//                    return jsonObject.toString();
//                }
//            }
//
//            //Veritabanina istegi loglama
//            //Instant start = Instant.now();
//            //long oid = insertRequestLog(serviceRequest, serviceName, methodName, start);
//
//            //SERVIS CAGRIM
//
//            //Veritabanina yaniti loglama
//            Instant finish = Instant.now();
//            if(serviceResponse != null) {
//               // insertResponseLog(serviceResponse.toString(), oid, start, finish);
//            }
//
//            logger.info("");
//            JSONObject jsonObject=new JSONObject();
//            jsonObject.accumulate("resultCode",1);
//            return jsonObject.toString();
//            //return encryptElementAndReturn(serviceResponse, methodName);
//        } catch (Exception e2) {
//            if("org.springframework.dao.DuplicateKeyException".equals(e2.getClass().getName())){
//                logger.error("DuplicateKeyException: "+ serviceRequest.toString());
//                return classicError("Bir hata oluştu, lütfen tekrar deneyiniz.");
//            }
//            logger.error("UnknownError: ");
//            e2.printStackTrace();
//            return unknownError();
//        }
//    }
//
//    @RequestMapping(value="activateAccount", method = RequestMethod.GET)
//    @CrossOrigin(value = "*")
//    //public @ResponseBody String activateAccount(
//    public void activateAccount(
//                HttpServletRequest request, HttpServletResponse response,
//                @RequestParam("activationToken") String activationToken) {
//        Instant start = Instant.now();
//        String email = dbUtil.findUserEmailById(activationToken);
//        if(email == null){
//            //BUNU ERROR PAGE e redirect et
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hatalı Aktivasyon İsteği");
//        }
//        dbUtil.updateUserById(activationToken,1,getLocalDateTime(start));
//
//        // Burada gelen transactionId'yi bir tabloya kaydet ve
//        // url tekrar tetiklenirse bu hesap bu url ile daha önce aktive edildi mesajı dön.
//        // yeni bir Id ile bu userı kaydet.
//
//        //Aktivasyon Tamamlandi, LOGIN yonlendir
//        String redirectUrl = "http://localhost:3000";
//        if ("prod".equals(EnvironmentConfig.getProfile())) {
//            redirectUrl = "PROD URL";
//        } else if ("test".equals(EnvironmentConfig.getProfile())) {
//            redirectUrl = "TEST URL";
//        }
//        redirectUrl = redirectUrl + "/activation";
////                "?activation=1";
////                "basketAmount=500.00&";
//        try {
//            response.sendRedirect(redirectUrl);
//        }catch(IOException e) {
//            logger.error("UnknownError: ");
//            e.printStackTrace();
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Page Not Found", e);
//        }
//    }
//
//
//    private long insertRequestLog(JSONObject jsonObject, String serviceName, String methodName,Instant start) throws JSONException {
//        return dbUtil.insertRequestLog(jsonObject.toString(), serviceName, methodName, getLocalDateTime(start));
//    }
//
//    private void insertResponseLog(String response, long oid, Instant start, Instant finish) {
//        long timeElapsed= Duration.between(start,finish).toMillis();
//        dbUtil.insertResponseLog(response, oid,timeElapsed,getLocalDateTime(finish));
//    }
//
//    private String getLocalDateTime(Instant instant) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime localDateTimeStart = LocalDateTime.ofInstant(instant, ZoneId.of("Europe/Istanbul"));
//        return localDateTimeStart.format(formatter);
//    }
//
//    private CaptchaResult isCaptchaValidated(String methodName, JSONObject serviceRequest){
//        try {
//            if ((methodName.equals("Login") || methodName.equals("Register")) && serviceRequest.has("captcha")){
//                if(JSONObject.NULL.equals(serviceRequest.get("captcha"))){
//                    //Tekrar Gonder Butonundan Geliyor
//                    return new CaptchaResult(true, "Captcha Is Valid.");
//                }
//                String captcha = serviceRequest.getString("captcha");
//                Boolean isValidCaptcha = captchaService.validateCaptcha(captcha);
//                if (!isValidCaptcha) {
//                    String message = "Geçersiz Giriş, lütfen tekrar deneyiniz";
//                    logger.error(message);
//                    return new CaptchaResult(false, message);
//                } else {
//                    return new CaptchaResult(true, "Captcha Is Valid.");
//                }
//            }
//            else {
//                return new CaptchaResult(true, "");
//            }
//        }catch(Exception e){
//            logger.error("Captcha Validation Error: " + serviceRequest.toString());
//            e.printStackTrace();
//            return new CaptchaResult(false, "Teknik Hata");
//        }
//    }
//
//    private boolean isAllowedServiceCall(String serviceName, String methodName) {
//        return UsableServices.anyMatch(serviceName, methodName);
//    }
//
//    private boolean isTransactionIdUsable(JSONObject serviceRequest) {
//        if(!serviceRequest.has("transactionId") || serviceRequest.isNull("transactionId")
//                || (serviceRequest.has("transactionId") && !serviceRequest.isNull("transactionId") && serviceRequest.getString("transactionId").length() !=36)
//        ){
//            return false;
//        }else{
//            return true;
//        }
//    }
//
//    private boolean isTransactionTimeUp(JSONObject serviceRequest) throws Exception {
//        String lastCallDate = null;
//
//        try {
//            lastCallDate = dbUtil.getTransactionStartDate(serviceRequest.getString("transactionId"));
//        }catch(Exception e){
//            logger.error("Credit - TimeoutCheck - DB Connection or DB Dataset error: " + e.getStackTrace());
//            throw e;
//        }
//
//        if(lastCallDate != null){
//            DateTimeFormatter formatter = DateTimeFormatter .ofPattern("yyyy-MM-dd HH:mm:ss");
//            LocalDateTime lastCallDateTime = LocalDateTime.parse(lastCallDate, formatter);
//            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Europe/Istanbul"));
//            if(lastCallDateTime.plusMinutes(12).isBefore(localDateTime)){
//                return true;
//            }
//            else{
//                return false;
//            }
//        }else{
//            return false;
//        }
//    }
//
//    private String captchaNotValidJSON(String errorMessage) throws JSONException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.accumulate("resultCode", 0);
//        jsonObject.accumulate("errorMessage", errorMessage);
//        jsonObject.accumulate("referenceCode", "");
//        jsonObject.accumulate("adkOid", "");
//        return jsonObject.toString();
//    }
//
//    private String unknownError() throws JSONException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.accumulate("resultCode", 0);
//        jsonObject.accumulate("errorMessage", "Bilinmeyen bir hata oluştu");
//        return jsonObject.toString();
//    }
//
//    private String classicError(String errorMessage) throws JSONException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.accumulate("resultCode", 0);
//        jsonObject.accumulate("errorMessage", errorMessage);
//        return jsonObject.toString();
//    }
//
//    private JSONObject abc() throws Exception{
//        JSONObject esbJsonRequest = new JSONObject();
//        esbJsonRequest.put("companyName","N11.com");
//        JSONObject jsonObj= new JSONObject();
//        jsonObj.put("categoryCode", "n11banner");
//        jsonObj.put("productType", "n11banner");
//        jsonObj.put("productName", "n11banner");
//        jsonObj.put("productAmount", "500");
//        jsonObj.put("count", "1");
//        jsonObj.put("total", "500");
//        JSONArray jsonArray = new JSONArray();
//        jsonArray.put(jsonObj);
//        esbJsonRequest.put("productTable",jsonArray);
//
//        return esbJsonRequest;
//    }
//
//    //PDF İçeriği iletme
//    private ResponseEntity<InputStreamResource> xyz(@PathVariable String docId){
//        ByteArrayInputStream in=null;
//        String DOCUMENT_STORAGE_URL = "EnvironmentConfig.documentStorageUrlPrefix + decryptedDocumentId+EnvironmentConfig.documentStorageUrlSuffix";
//        try {
//            logger.info("DocumentService Call - documentID: " + " URL: " + DOCUMENT_STORAGE_URL);
//            in=download(DOCUMENT_STORAGE_URL);
//        }catch (Exception e){
//            logger.error("DocumentService Call - UnknownError - documentID: " + " URL: " + DOCUMENT_STORAGE_URL);
//            e.printStackTrace();
//        }
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime localDateTime=LocalDateTime.now();
//        String formattedLocalDateTime = localDateTime.format(formatter);
//
//        HttpHeaders headers = new HttpHeaders();
//
//        return ResponseEntity
//                .ok()
//                .headers(headers)
//                .body(new InputStreamResource(in));
//
//    }
//
//    public static ByteArrayInputStream download(String urlname) throws IOException {
//        URL url = new URL(urlname);
//        HttpURLConnection conn = null;
//        InputStream stream = null;
//        try {
//            conn = (HttpURLConnection) url.openConnection();
//            stream = conn.getInputStream();
//            final byte[] bytes = IOUtils.toByteArray(stream);
//            return new ByteArrayInputStream(bytes);
//        }
//        finally {
//            if (null != conn) {
//                conn.disconnect();
//            }
//            if (null != stream) {
//                IOUtils.closeQuietly(stream);
//            }
//        }
//    }
//}