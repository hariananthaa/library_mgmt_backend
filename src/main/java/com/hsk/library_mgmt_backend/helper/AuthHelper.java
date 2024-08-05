package com.hsk.library_mgmt_backend.helper;

import com.hsk.library_mgmt_backend.response.ResponseData;
import com.hsk.library_mgmt_backend.service.AuthService;
import com.hsk.library_mgmt_backend.web.v1.payload.AuthResponse;
import com.hsk.library_mgmt_backend.web.v1.payload.AuthenticationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
@RequiredArgsConstructor
public class AuthHelper {
    private final AuthService authService;

    public ResponseData<AuthResponse> authenticate(AuthenticationRequest authenticationRequest, BindingResult bindingResult) {
        BindingResultHelper.processBindingResult(bindingResult);
        return authService.authenticate(authenticationRequest);
    }
}
