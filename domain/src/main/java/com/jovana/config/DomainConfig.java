package com.jovana.config;

import com.jovana.entity.EntityPackage;
import com.jovana.exception.ExceptionPackage;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

/**
 * Created by jovana on 24.02.2020
 */
@Configuration
@ComponentScan(basePackageClasses = {EntityPackage.class, ExceptionPackage.class})
@EntityScan(basePackageClasses = {EntityPackage.class, Jsr310JpaConverters.class})
public class DomainConfig {
}
