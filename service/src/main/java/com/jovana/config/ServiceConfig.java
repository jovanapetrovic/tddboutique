package com.jovana.config;

import com.jovana.service.ServicePackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by jovana on 24.02.2020
 */
@Configuration
@ComponentScan(basePackageClasses = ServicePackage.class)
public class ServiceConfig {

    @Autowired
    private Environment environment;

    @Bean
    public String getImageUploadDir() {
        return this.environment.getProperty("file.uploadDir");
    }

    @Bean
    public String getStripeApiKey() {
        return this.environment.getProperty("stripe.apiKey");
    }

}
