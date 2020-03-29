package com.jovana.config;

import com.jovana.exception.ExceptionHandlerPackage;
import com.jovana.api.RestPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;
import java.util.Arrays;

/**
 * Created by jovana on 24.02.2020
 */
@Configuration
@EnableSwagger2
@ComponentScan(basePackageClasses = {RestPackage.class, ExceptionHandlerPackage.class})
public class RestConfig {

    Class[] ignoreClass = {InputStreamResource.class, File.class};

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .globalOperationParameters(Arrays.asList(
                        new ParameterBuilder()
                                .name("Authorization")
                                .modelRef(new ModelRef("string"))
                                .description("JWT token; unique per user, response of api/login request")
                                .parameterType("header")
                                .required(true)
                                .build()))
                .groupName("Users")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jovana.api"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(authorization()))
                .ignoredParameterTypes(ignoreClass)
                ;
    }

    private ApiKey authorization(){
        return new ApiKey(HttpHeaders.AUTHORIZATION, "JWT", "header");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Web Boutique TDD project")
                .description("\nMy web Boutique project made in TDD manner in Spring Boot\n")
                .version("1.0")
                .build();
    }

}
