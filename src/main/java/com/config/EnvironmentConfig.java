package com.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import java.math.BigInteger;

@Component
//@PropertySource({"file:${spring.config.location}"})
public class EnvironmentConfig {

    public static String profile;

    @Value("${app.profile}")
    public void setProfile(String profile){
        this.profile=profile;
    }

    public static String decrypt(String sOrigHex) {
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

    public static byte[] toByteArray(String s, int i, int j) {
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
}