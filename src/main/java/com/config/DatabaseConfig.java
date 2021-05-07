package com.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;
import java.math.BigInteger;
import java.sql.SQLException;

@ComponentScan
@Configuration
@EnableTransactionManagement
//@PropertySource({"file:${spring.config.location}"})
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    private static String username;
    private static String password;
    private static String url;

    @Value("${spring.datasource.username}")
    public void setUsername(String username) {
      this.username = username;
    }
    @Value("${spring.datasource.password}")
    public void setPassword(String password) {
        this.password = password;
    }
    @Value("${spring.datasource.url}")
    public void setUrl(String url) {
        this.url = url;
    }

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        try {
            dataSource.getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            logger.error("Database connection error. Error: ");
            e.printStackTrace();
        }
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource)
    {
        return new JdbcTemplate(dataSource);
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

    public static String encrypt(String sOrig) {
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

    public static String toString(byte[] abyte0) {
        return toString(abyte0, 0, abyte0.length, true);
    }

    public static String toString(byte[] abyte0, int i, int j, boolean flag) {
        String s = flag ? "0123456789ABCDEF" : "0123456789abcdef";
        StringBuffer stringbuffer = new StringBuffer(j * 2);

        for(int k = i + j; i < k; ++i) {
            stringbuffer.append(s.charAt((abyte0[i] & 240) >>> 4));
            stringbuffer.append(s.charAt(abyte0[i] & 15));
        }

        return stringbuffer.toString();
    }

}
