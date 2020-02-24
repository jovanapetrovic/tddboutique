package com.jovana.config;

import com.jovana.repositories.RepositoryPackage;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by jovana on 24.02.2020
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = RepositoryPackage.class)
public class RepositoryConfig {
}
