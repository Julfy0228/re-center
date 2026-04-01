package com.recenter.config;

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
import java.nio.file.Paths;

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
                    jakarta.validation.Path.Node traversableProperty,
                    Class<?> rootBeanType,
                    jakarta.validation.Path pathToTraversableObject,
                    ElementType elementType) {
                return true;
            }

            @Override
            public boolean isCascadable(
                    Object traversableObject,
                    jakarta.validation.Path.Node traversableProperty,
                    Class<?> rootBeanType,
                    jakarta.validation.Path pathToTraversableObject,
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
        java.nio.file.Path currentDir = Paths.get("").toAbsolutePath().normalize();
        java.nio.file.Path uploadsPath = currentDir.getFileName() != null
                && "backend".equalsIgnoreCase(currentDir.getFileName().toString())
                ? currentDir.resolve("uploads")
                : currentDir.resolve(Paths.get("backend", "uploads"));

        String uploadLocation = uploadsPath
                .toAbsolutePath()
                .normalize()
                .toUri()
                .toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadLocation);

        registry.addResourceHandler("/**")
                .addResourceLocations("/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}
