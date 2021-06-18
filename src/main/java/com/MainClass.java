package com;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import javax.annotation.PostConstruct;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class MainClass extends SpringBootServletInitializer {

    public static void main(String[]args){
        SpringApplication.run(MainClass.class,args);
    }

    @PostConstruct //It runs once, right after the beans are initialized.
    public void postConstruct() {
        System.out.println("1 - PostConstruct");
    }

    @Bean
    InitializingBean initializingBean() { //It works pretty similarly to PostConstruct
        System.out.println("2 - InitializingBean");
        return () -> {
        };
    }

    @EventListener
    public void contextRefreshedEvent(ContextRefreshedEvent event) { //This is running logic after the Spring context has been initialized
        System.out.println("3 - EventListener");
    }

    @Bean
    public CommandLineRunner commandLineRunner() { //It works at the end of application startup after the Spring application context has been initialized
        return (args) -> {
            System.out.println("4 - CommandLineRunner");
        };
    }

//    @Bean
//    public JavaMailSender getJavaMailSender() {
//    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//    mailSender.setHost("smtp.gmail.com");
//    mailSender.setPort(465);
//    mailSender.setUsername("gosterbilgini@gmail.com");
//    mailSender.setPassword("Beerussama1");
//
//    Properties props = mailSender.getJavaMailProperties();
//    props.put("mail.transport.protocol", "smtp");
//    props.put("mail.smtp.auth", "true");
//    props.put("mail.smtp.starttls.enable", "true");
//    props.put("mail.debug", "true");
//
//    return mailSender;
//  }
}
