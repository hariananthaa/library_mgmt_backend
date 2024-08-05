package com.hsk.library_mgmt_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * Configuration class for setting up Cross-Origin Resource Sharing (CORS) settings.
 * <p>
 * This class configures CORS to allow cross-origin requests from specified origins
 * and enables various HTTP methods and headers for API endpoints.
 * </p>
 */
@Configuration
public class CorsConfig {

    /**
     * The allowed origins for CORS requests. This value is populated from
     * the application properties or defaults to an empty array if not specified.
     */
    @Value("${allowed-origins:[]}")
    String[] allowedOrigins;

    /**
     * Provides a {@link CorsConfigurationSource} bean that configures CORS settings.
     * <p>
     * This method sets the allowed origins, methods, and headers for CORS requests,
     * and allows credentials to be included in the requests. It applies this configuration
     * to all endpoints in the application.
     * </p>
     *
     * @return a {@link CorsConfigurationSource} that defines the CORS configuration.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(allowedOrigins));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials, if needed

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply this configuration to all endpoints

        return source;
    }

    /**
     * Provides a {@link CorsFilter} bean that applies the CORS configuration to incoming requests.
     * <p>
     * This filter ensures that CORS settings are enforced for all requests to the application.
     * </p>
     *
     * @return a {@link CorsFilter} that uses the configured {@link CorsConfigurationSource}.
     */
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}
