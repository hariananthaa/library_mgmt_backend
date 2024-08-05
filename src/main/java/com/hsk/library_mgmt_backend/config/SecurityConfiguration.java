package com.hsk.library_mgmt_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class that sets up the security policies for the application.
 * <p>
 * This configuration includes setting up HTTP security, CORS (Cross-Origin Resource Sharing),
 * CSRF (Cross-Site Request Forgery) protection, session management, and authentication.
 * </p>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Configures the security filter chain for the application.
     *
     * @param httpSecurity the HttpSecurity object used to configure security settings.
     * @return the configured SecurityFilterChain object.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(AbstractHttpConfigurer::disable) // Disables CSRF protection
                .cors(Customizer.withDefaults()) // Enables default CORS configuration
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/v1/auth/**").permitAll() // Allows public access to Swagger UI and authentication endpoints
                                .anyRequest().authenticated() // Requires authentication for all other requests
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configures session management to be stateless
                )
                .authenticationProvider(authenticationProvider) // Sets the custom AuthenticationProvider
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Adds the JWT filter before the default UsernamePasswordAuthenticationFilter

        return httpSecurity.build();
    }
}
