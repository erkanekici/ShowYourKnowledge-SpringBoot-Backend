package com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionDecryptionAES {

    private static final Logger logger = LoggerFactory.getLogger(EncryptionDecryptionAES.class);
    private static final String key = "FBMWK2WtihHracSGJKsw2ier2130jbcQ"; //256 bit key

    public static String encrypt(String value) throws Exception {
        try{
            byte[] IV = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(IV);
            IvParameterSpec ivSpec = new IvParameterSpec(IV);

            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"),"AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getUrlEncoder().encodeToString(encrypted).concat((Base64.getUrlEncoder().encodeToString(cipher.getIV())));
        } catch (Exception ex) {
          logger.error("Unexpected error: "+value);
          throw ex;
        }
    }

    public static String decrypt(String value) throws Exception{
        try{
            byte[] IV= Base64.getUrlDecoder().decode((value.substring(44)));
            IvParameterSpec ivSpec = new IvParameterSpec(IV);

            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"),"AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE,skeySpec,ivSpec);
            byte[] decryptedText = cipher.doFinal(Base64.getUrlDecoder().decode(value.substring(0,44)));
            return new String(decryptedText);
        } catch (Exception ex) {
            logger.error("Unexpected error: "+value);
            throw ex;
        }
    }
}
