package com.recenter.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class);

        servletContext.addListener(new ContextLoaderListener(rootContext));

        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(WebConfig.class);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        String tempDir = System.getProperty("java.io.tmpdir") + File.separator + "re-center-uploads";

        try {
            Files.createDirectories(Paths.get(tempDir));
        } catch (Exception e) {
            System.err.println("Failed to create temp upload directory: " + e.getMessage());
        }
        
        dispatcher.setMultipartConfig(new MultipartConfigElement(tempDir, 10_000_000, 20_000_000, 1_048_576));
    }
}
