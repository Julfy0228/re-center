package com.recenter.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.recenter")
@EnableTransactionManagement
@Import({DataSourceConfig.class, JpaConfig.class, SecurityConfig.class})
public class AppConfig {
}