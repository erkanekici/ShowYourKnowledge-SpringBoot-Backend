package com.config.swagger;

import com.config.EnvironmentConfig;
import com.controller.constants.ApiConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

	@Bean
	public Docket userApiDoc() {
		return new SwaggerConfigurer(ApiConstants.ApiGroups.UserInfoApi.NAME, ApiConstants.ApiGroups.UserInfoApi.TITLE, ApiConstants.ApiGroups.UserInfoApi.DESCRIPTION,
				ApiConstants.ApiGroups.UserInfoApi.PATH, EnvironmentConfig.getProjectVersion()).build();
	}

//	@Bean
//	public Docket documentApiDoc() {
//		return new SwaggerConfigurer(ApiConstants.ApiGroups.DocumentApi.NAME, ApiConstants.ApiGroups.DocumentApi.TITLE, ApiConstants.ApiGroups.DocumentApi.DESCRIPTION,
//				ApiConstants.ApiGroups.DocumentApi.PATH, EnvironmentConfig.getProjectVersion()).build();
//	}

}
