package com.jovana.config;

import com.jovana.exception.ExceptionHandlerPackage;
import com.jovana.resources.RestPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jovana on 24.02.2020
 */
@Configuration
@ComponentScan(basePackageClasses = {RestPackage.class, ExceptionHandlerPackage.class})
public class RestConfig {
}
