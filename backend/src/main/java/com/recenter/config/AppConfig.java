package com.recenter.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {
        "com.recenter.service",
        "com.recenter.repository"
})
@EnableTransactionManagement
@Import({DataSourceConfig.class, JpaConfig.class})
public class AppConfig {
}