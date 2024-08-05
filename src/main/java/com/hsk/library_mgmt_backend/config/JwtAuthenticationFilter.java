package com.hsk.library_mgmt_backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter for handling JWT authentication in HTTP requests.
 * <p>
 * This filter is responsible for parsing the JWT token from the `Authorization` header,
 * validating it, and setting the authentication in the Spring Security context if the token is valid.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Filters incoming HTTP requests for JWT authentication.
     * <p>
     * This method checks for the presence of the `Authorization` header and verifies if it starts with "Bearer ".
     * If a valid JWT is found, it extracts the username, loads user details, validates the token,
     * and sets the authentication in the Spring Security context.
     * </p>
     *
     * @param request the {@link HttpServletRequest} object that contains the request details.
     * @param response the {@link HttpServletResponse} object used to send the response.
     * @param filterChain the {@link FilterChain} used to pass the request and response to the next filter in the chain.
     * @throws ServletException if a servlet-specific error occurs during the filtering process.
     * @throws IOException if an I/O error occurs during the filtering process.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Check if the Authorization header is present and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
        } else {
            jwt = authHeader.substring(7); // Extract the JWT from the header
            userEmail = jwtService.extractUsername(jwt); // Extract the username from the JWT

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Load user details from the email
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // Validate the JWT
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // Create an authentication token and set it in the security context
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response); // Proceed to the next filter in the chain
        }
    }
}
