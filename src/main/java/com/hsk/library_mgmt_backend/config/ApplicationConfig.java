package com.hsk.library_mgmt_backend.config;

import com.hsk.library_mgmt_backend.persistent.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.TimeZone;

/**
 * Configuration class for Spring Security settings.
 * <p>
 * This class configures the components required for authentication and password encoding.
 * It sets the default time zone for the application and provides beans for
 * {@link UserDetailsService}, {@link AuthenticationProvider}, {@link AuthenticationManager},
 * and {@link PasswordEncoder}.
 * </p>
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final MemberRepository memberRepository;

    /**
     * Sets the default time zone for the application to "Asia/Kolkata".
     * This method is called after the bean initialization.
     */
    @PostConstruct
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
    }

    /**
     * Provides a {@link UserDetailsService} bean that retrieves user details from the repository.
     * <p>
     * This service is used by Spring Security to load user-specific data during authentication.
     * </p>
     *
     * @return a {@link UserDetailsService} that loads user details from the {@link MemberRepository}.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> memberRepository.findByEmail(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Member not found.")
                );
    }

    /**
     * Provides an {@link AuthenticationProvider} bean configured with a {@link UserDetailsService}
     * and {@link PasswordEncoder}.
     * <p>
     * This provider is used to authenticate users based on their credentials.
     * </p>
     *
     * @return a {@link DaoAuthenticationProvider} configured with the {@link UserDetailsService}
     *         and {@link PasswordEncoder}.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Provides an {@link AuthenticationManager} bean.
     * <p>
     * This manager is used to handle authentication requests.
     * </p>
     *
     * @param config the {@link AuthenticationConfiguration} used to retrieve the {@link AuthenticationManager}.
     * @return an {@link AuthenticationManager} bean.
     * @throws Exception if there is an issue retrieving the authentication manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Provides a {@link PasswordEncoder} bean that uses {@link BCryptPasswordEncoder} for encoding passwords.
     * <p>
     * This encoder is used to securely encode passwords before storing them.
     * </p>
     *
     * @return a {@link BCryptPasswordEncoder} for password encoding.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
