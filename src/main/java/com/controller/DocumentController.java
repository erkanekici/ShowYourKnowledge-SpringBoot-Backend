package com.controller;

import com.repository.DBUtil;
import com.util.EncryptionDecryptionAES;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController()
@CrossOrigin(value="*")
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    DBUtil dbTransactions;

    @RequestMapping(value = "/document/{documentID}/{transactionID}")
    public ResponseEntity<InputStreamResource> viewDocument(@PathVariable String documentID,@PathVariable String transactionID) {
        logger.info("DocumentService Call - transactionID: " + transactionID + " - encryptedDocumentID: " + documentID);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=dosya.pdf");
        // headers.add("Content-Disposition", "inline;filename=dosya.pdf");
        // headers.setContentType(MediaType.parseMediaType("application/pdf"));

        String lastCallDate = null;
        try{
            lastCallDate = dbTransactions.getTransactionStartDateByDocumentId(transactionID,documentID);
        } catch (Exception e){
            logger.error("DocumentService Call - DB Connection Error: " + documentID + " Error Detail: " + e.getStackTrace());
        }
        if(lastCallDate != null){
            DateTimeFormatter formatter = DateTimeFormatter .ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime lastCallDateTime = LocalDateTime.parse(lastCallDate, formatter);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Europe/Istanbul"));

            if(lastCallDateTime.plusMinutes(12).isBefore(localDateTime)){
                return ResponseEntity
                        .ok()
                        .headers(headers)
                        .body(new InputStreamResource(new ByteArrayInputStream(hexStringToByteArray("5a616d616e20646f6c6475")))); // --> Zaman doldu
            }
        }

        //decrypt documentId
        String decryptedDocumentId = decryptDocumentId(documentID,transactionID);

        ByteArrayInputStream in=null;
        //String DOCUMENT_STORAGE_URL = EnvironmentConfig.documentStorageUrlPrefix + decryptedDocumentId + EnvironmentConfig.documentStorageUrlSuffix;
        String DOCUMENT_STORAGE_URL ="";
        try {
            in=download(DOCUMENT_STORAGE_URL);
        }catch (Exception e){
            logger.error("DocumentService Call - Error - transactionID: " + transactionID + " - URL: " + DOCUMENT_STORAGE_URL);
            e.printStackTrace();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        String formattedLocalDateTime = localDateTime.format(formatter);

        //dbTransactions.insertRequestLog(documentID,"","document", documentID, formattedLocalDateTime, transactionID);
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Executes a download syncronously from a remote server
     * @param urlname the remote url file to fetch
     * @return The downloaded stream as {@link java.io.ByteArrayInputStream}
     * @throws java.io.IOException
     */
    public static ByteArrayInputStream download(String urlname) throws IOException {
        URL url = new URL(urlname);
        HttpURLConnection conn = null;
        InputStream stream = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            stream = conn.getInputStream();
            final byte[] bytes = IOUtils.toByteArray(stream);
            return new ByteArrayInputStream(bytes);
        }
        finally {
            if (null != conn) {
                conn.disconnect();
            }
            if (null != stream) {
                IOUtils.closeQuietly(stream);
            }
        }
    }

    private String decryptDocumentId(String documentID, String transactionID) {
        if (documentID != null && !documentID.isEmpty()) {
            try {
                return EncryptionDecryptionAES.decrypt(documentID,44);
            }
            catch (Exception e) {
                logger.error("DocumentService Call - Error Unencrypted documentID - transactionID: " + transactionID + " - documentID: " + documentID);
                return null;
            }
        }
        else {
            return null;
        }
    }
}