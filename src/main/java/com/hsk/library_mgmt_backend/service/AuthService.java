package com.hsk.library_mgmt_backend.service;

import com.hsk.library_mgmt_backend.response.ResponseData;
import com.hsk.library_mgmt_backend.web.v1.payload.AuthResponse;
import com.hsk.library_mgmt_backend.web.v1.payload.AuthenticationRequest;

public interface AuthService {
    ResponseData<AuthResponse> authenticate(AuthenticationRequest authenticationRequest);
}
