package com.common.crypto;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class EncryptionAndHashingUtil {

    private static EncryptionAndHashingUtil _this = null;
    public static EncryptionAndHashingUtil getInstance() {
        if (_this == null)
            _this = new EncryptionAndHashingUtil();
        return _this;
    }

    private static final Logger logger = LoggerFactory.getLogger(EncryptionAndHashingUtil.class);
    private static final String key = "FBMWK2WtihHracSGJKsw2ier2130jbcQ"; //256 bit key

    /** AES ENCRYPT-DECRYPT */

    public String encryptAES(String value) throws Exception {
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

    public String decryptAES(String value, int num) throws Exception{
        try{
            byte[] IV= Base64.getUrlDecoder().decode((value.substring(44)));
            IvParameterSpec ivSpec = new IvParameterSpec(IV);

            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"),"AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE,skeySpec,ivSpec);
            byte[] decryptedText = cipher.doFinal(Base64.getUrlDecoder().decode(value.substring(0,num)));
            return new String(decryptedText);
        } catch (Exception ex) {
            logger.error("Unexpected error: "+value);
            throw ex;
        }
    }

    /** CUSTOM ENCRYPT-DECRYPT */

    public String decrypt(String sOrigHex) {
        String sOrig = new String(toByteArray(sOrigHex,0,sOrigHex.length()));
        String k = sOrig.substring(4, sOrig.length() - 4);
        StringBuffer x = new StringBuffer();
        for (int i = 0; i < k.length(); i++) {
            int ich = (int) k.charAt(i);
            x.append((char) ich);
        }
        String s = sOrig.substring(0, 4) + sOrig.substring(sOrig.length() - 4);
        return new String(new BigInteger(x.toString().getBytes()).xor(new BigInteger(s.getBytes())).toByteArray());
    }

    public String encrypt(String sOrig) {
        String e = null;
        String s = null;

        java.util.Random rand = new java.util.Random();
        try {
            Thread.currentThread().sleep(50);
        } catch (Exception ex) {
        }
        rand.setSeed(new java.util.Date().getTime());
        s = String.valueOf(rand.nextInt());
        if (s.length() < 8) {
            int k = s.length();
            for (int j = 0; j < 8 - k; j++) {
                s += "1";
            }
        }
        s = s.substring(s.length() - 8);
        e = new String(new BigInteger(sOrig.getBytes()).xor(new BigInteger(s.getBytes())).toByteArray());
        return toString((s.substring(0, 4) + e + s.substring(4)).getBytes());
    }

    private byte[] toByteArray(String s, int i, int j) {
        if (j % 2 != 0) {
            throw new IllegalArgumentException("Illegal length of Hex encoding: " + j + " (not n*2)");
        } else if (j == 0) {
            return new byte[0];
        } else {
            byte[] abyte0 = new byte[j / 2];
            int k = 0;
            int l = 0;
            for(int i1 = i + j; i < i1; ++l) {
                int j1 = Character.digit(s.charAt(i), 16);
                if (j1 < 0) {
                    throw new IllegalArgumentException("Illegal characters in Hex encoding: " + s.charAt(i));
                }
                k = (k << 4) + j1;
                if (l % 2 == 1) {
                    abyte0[l / 2] = (byte)k;
                }
                ++i;
            }
            return abyte0;
        }
    }

    private String toString(byte[] abyte0) {
        return toString(abyte0, 0, abyte0.length, true);
    }

    private String toString(byte[] abyte0, int i, int j, boolean flag) {
        String s = flag ? "0123456789ABCDEF" : "0123456789abcdef";
        StringBuffer stringbuffer = new StringBuffer(j * 2);

        for(int k = i + j; i < k; ++i) {
            stringbuffer.append(s.charAt((abyte0[i] & 240) >>> 4));
            stringbuffer.append(s.charAt(abyte0[i] & 15));
        }

        return stringbuffer.toString();
    }

    /** HASH */

    private String hashFile(File file) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            return DigestUtils.md5Hex(is);
        } catch (Exception exception) {
            return UUID.randomUUID().toString();
        } finally {
            close(is);
        }
    }

    private void close(InputStream is) {
        if (null != is) {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
    }
}
