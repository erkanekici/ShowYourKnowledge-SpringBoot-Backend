package com.common.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

//TODO TEST Et
public class TripleDES {

    private String key;
    private String initializationVector;

    public TripleDES(String key, String initializationVector) {
        this.key = key;
        this.initializationVector = initializationVector;
    }

    public String encryptText(String plainText) throws Exception {
        // ---- Use specified 3DES key and IV from other source --------------
        byte[] plaintext = plainText.getBytes();
        byte[] tdesKeyData = key.getBytes();
        // byte[] myIV = initializationVector.getBytes();
        Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
        IvParameterSpec ivspec = new IvParameterSpec(initializationVector.getBytes());
        c3des.init(Cipher.ENCRYPT_MODE, myKey, ivspec);
        byte[] cipherText = c3des.doFinal(plaintext);
        return Base64.getUrlEncoder().encodeToString(cipherText);
    }

    public String decryptText(String cipherText) throws Exception {
        byte[] encData = Base64.getUrlDecoder().decode(cipherText);
        Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        byte[] tdesKeyData = key.getBytes("UTF8");
        SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
        IvParameterSpec ivspec = new IvParameterSpec(initializationVector.getBytes("UTF8"));
        decipher.init(Cipher.DECRYPT_MODE, myKey, ivspec);
        byte[] plainText = decipher.doFinal(encData);
        return new String(plainText);
    }
}