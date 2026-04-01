package com.recenter.config;

import jakarta.validation.Path;
import jakarta.validation.TraversableResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.annotation.ElementType;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.recenter.controller")
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setTraversableResolver(new TraversableResolver() {
            @Override
            public boolean isReachable(
                    Object traversableObject,
                    Path.Node traversableProperty,
                    Class<?> rootBeanType,
                    Path pathToTraversableObject,
                    ElementType elementType) {
                return true;
            }

            @Override
            public boolean isCascadable(
                    Object traversableObject,
                    Path.Node traversableProperty,
                    Class<?> rootBeanType,
                    Path pathToTraversableObject,
                    ElementType elementType) {
                return true;
            }
        });
        return validatorFactoryBean;
    }

    @Override
    public Validator getValidator() {
        return validator();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}
