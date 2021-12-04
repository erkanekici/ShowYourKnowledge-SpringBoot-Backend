package com.config;

import com.common.operatorUtils.TextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
//@PropertySource({"file:${spring.config.location}"}) //To read the config file from outside the project
public class EnvironmentConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentConfig.class);


    private static String profile;
    private static String projectVersion;

    @Value("${spring.profiles.active:dev}")
    public void setProfile(String profile){
        LOGGER.info("Spring profile: {}", profile);
        this.profile = profile;
    }

    @Value("${pom.version}")
    public void setProjectVersion(String projectVersion){
        LOGGER.info("Project version: {}", projectVersion);
        this.projectVersion = projectVersion;
    }

    public static String getProfile() {
        return profile;
    }

    public static String getProjectVersion() {
        return projectVersion;
    }



    //TODO Test Et
    private Environment env;

    @Autowired
    public EnvironmentConfig(Environment env) {
        this.env = env;
    }

    public Optional<Boolean> readBoolean(String propertyKey) {
        String propertyValue = env.getProperty(propertyKey);
        if (!TextUtil.isNullOrEmptyAfterTrim(propertyValue)) {
            boolean boolValue = Boolean.parseBoolean(propertyValue);
            return Optional.of(boolValue);
        } else {
            return Optional.empty();
        }
    }

    public Optional<String> readString(String propertyKey) {
        String propertyValue = env.getProperty(propertyKey);
        return Optional.ofNullable(propertyValue);
    }

    public void getActiveProfiles() {
        for (String profileName : env.getActiveProfiles()) {
            System.out.println("Currently active profile - " + profileName);
        }
    }

}