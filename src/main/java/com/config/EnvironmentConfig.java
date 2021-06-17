package com.config;

import com.common.operatorUtils.TextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
//@PropertySource({"file:${spring.config.location}"}) //config dosyasını proje dışından okumak için
public class EnvironmentConfig {

    private static String profile;
    private static String projectVersion;

    @Value("${spring.profiles.active}")
    public void setProfile(String profile){
        this.profile=profile;
    }

    @Value("${pom.version}")
    public void setProjectVersion(String projectVersion){
        this.projectVersion=projectVersion;
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