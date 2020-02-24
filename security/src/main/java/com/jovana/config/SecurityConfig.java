package com.jovana.config;

import com.jovana.auth.AuthPackage;
import com.jovana.token.TokenPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jovana on 24.02.2020
 */
@Configuration
@ComponentScan(basePackageClasses = {AuthPackage.class, TokenPackage.class})
public class SecurityConfig {
}
