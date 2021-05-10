package com.config.swagger;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SwaggerConfigurer {

    private final String group;
    private final String title;
    private final String description;
    private final String paths;
    private final String excludes;
    private final String version;

    public SwaggerConfigurer(String group, String title, String description, String paths, String version) {
        this(group, title, description, paths, null, version);
    }

    public SwaggerConfigurer(String group, String title, String description, String paths, String excludes, String version) {
        this.group = group;
        this.title = title;
        this.description = description;
        this.paths = paths;
        this.excludes = excludes;
        this.version = version;
    }

    public Docket build() {
        List<ResponseMessage> globalResponseMessages = globalResponseMessages();

        //@formatter:off
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(apis())
                .paths(apiPaths())
                .build()
                .groupName(group)
                .apiInfo(apiInfo())
                .pathMapping("/")
                .genericModelSubstitutes(ResponseEntity.class)
                .globalResponseMessage(RequestMethod.POST, globalResponseMessages)
                .globalResponseMessage(RequestMethod.GET, globalResponseMessages)
                .useDefaultResponseMessages(false)
                .enableUrlTemplating(false)
                .forCodeGeneration(!"test".equals(System.getProperty("spring.profiles.active")));
        //@formatter:on

        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(title).description(description).termsOfServiceUrl("N/A")
                .contact(defaultContact()).license(defaultLicense()).licenseUrl("None").version(version).build();
    }

    private Predicate<String> apiPaths() {
        if (StringUtils.isEmpty(excludes)) {
            return pathSelectors(paths);
        } else {
            return Predicates.and(pathSelectors(paths), Predicates.not(pathSelectors(excludes)));
        }
    }

    private Predicate<String> pathSelectors(String paths) {
        return Predicates.or(transform(Arrays.asList(paths.split(",")), PathSelectors::ant));
    }

    private Predicate<RequestHandler> apis() {
        return RequestHandlerSelectors.withClassAnnotation(RestController.class);
    }

    private Contact defaultContact() {
        return new Contact("gosterbilgini.com", "", "gosterbilgini@gmail.com");
    }

    private String defaultLicense() {
        return null;
    }

    private List<ResponseMessage> globalResponseMessages() {
        List<ResponseMessage> messages = new ArrayList<>();

        messages.add(response(HttpStatus.UNAUTHORIZED, "HTTP basic auth parameters are wrong or empty"));
        messages.add(response(HttpStatus.FORBIDDEN, "Credentials are not enough to view requested resource"));

        messages.add(response(HttpStatus.OK, "Request processed successfully"));
        messages.add(response(HttpStatus.CREATED, "New resource created successfully"));

        messages.add(response(HttpStatus.BAD_REQUEST, "Request parameters are invalid"));
        messages.add(response(HttpStatus.NOT_FOUND, "Requested resource was not found"));
        messages.add(response(HttpStatus.INTERNAL_SERVER_ERROR, "Server was unable to process the request"));

        messages.add(response(HttpStatus.BAD_GATEWAY, "Downstream server returned unknown response"));
        messages.add(response(HttpStatus.GATEWAY_TIMEOUT, "Downstream server did not return in time"));
        messages.add(response(HttpStatus.SERVICE_UNAVAILABLE, "Downstream server is not available"));

        return messages;
    }

    private ResponseMessage response(HttpStatus status, String message) {
        return new ResponseMessageBuilder().code(status.value()).message(message).build();
    }

    public static <T, O> List<O> transform(List<T> input, Function<T, O> func) {
        return transformStream(input, func).collect(Collectors.toList());
    }

    private static <T, O> Stream<O> transformStream(Collection<T> input, Function<T, O> func) {
        return input.stream().map(func).filter(Objects::nonNull);
    }
}