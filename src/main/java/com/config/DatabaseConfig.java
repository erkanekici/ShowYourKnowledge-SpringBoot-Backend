package com.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;

@ComponentScan
@Configuration
@EnableTransactionManagement
//@PropertySource({"file:${spring.config.location}"}) //config dosyasını proje dışından okumak için
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

        //JNDI kullanimi icin
//        Object lookup = null;
//        try {
//            Context context = new InitialContext();
//            lookup = context.lookup("java:/myDb");
//        } catch (NamingException e) {
//            logger.error("NamingException error. Error: ");
//            e.printStackTrace();
//        }
//
//        if(lookup != null){
//            DriverManagerDataSource dataSource = (DriverManagerDataSource) lookup;
//        } else{
//            logger.error("JNDI Context lookup null");
//            throw new RuntimeException("JNDI Context lookup error");
//        }

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

}
