package com.hsk.library_mgmt_backend.service.impl;

import com.hsk.library_mgmt_backend.config.JWTService;
import com.hsk.library_mgmt_backend.exception.NotFoundException;
import com.hsk.library_mgmt_backend.persistent.entity.Member;
import com.hsk.library_mgmt_backend.persistent.repository.MemberRepository;
import com.hsk.library_mgmt_backend.response.ResponseData;
import com.hsk.library_mgmt_backend.response.ResponseUtil;
import com.hsk.library_mgmt_backend.service.AuthService;
import com.hsk.library_mgmt_backend.web.v1.payload.AuthResponse;
import com.hsk.library_mgmt_backend.web.v1.payload.AuthenticationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the AuthService interface for handling authentication.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final JWTService jwtService;

    /**
     * Authenticates a user based on the provided authentication request.
     *
     * @param authenticationRequest the authentication request containing email and password
     * @return a ResponseData containing the authentication response with a JWT token
     * @throws NotFoundException if the user is not found
     */
    @Override
    public ResponseData<AuthResponse> authenticate(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );

        Member user = memberRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("name", user.getName());
        claims.put("phone", user.getPhone());
        claims.put("role", user.getRole());

        String jwtToken = jwtService.generateToken(user, claims);
        AuthResponse authResponse = new AuthResponse(jwtToken);

        return ResponseUtil.responseConverter(authResponse);
    }
}
