package com.lokesh.Project_Nota.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // This configuration allows your Vercel frontend and local development server
                // to make requests to your API.
                registry.addMapping("/api/**") // Apply CORS to all endpoints under /api
                        .allowedOrigins("https://project-nota-frontend.vercel.app", "http://localhost:5173") // List of trusted origins
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                        .allowedHeaders("*") // Allow all headers (like Authorization)
                        .allowCredentials(true);
            }
        };
    }
}
