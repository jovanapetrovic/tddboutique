package com.jovana.config;

import com.jovana.service.ServicePackage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jovana on 24.02.2020
 */
@Configuration
@EnableConfigurationProperties({
        ImageStorageProperties.class
})
@ComponentScan(basePackageClasses = ServicePackage.class)
public class ServiceConfig {
}
