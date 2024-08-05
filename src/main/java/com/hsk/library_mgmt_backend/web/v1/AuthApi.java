package com.hsk.library_mgmt_backend.web.v1;

import com.hsk.library_mgmt_backend.helper.AuthHelper;
import com.hsk.library_mgmt_backend.response.ResponseData;
import com.hsk.library_mgmt_backend.web.v1.payload.AuthResponse;
import com.hsk.library_mgmt_backend.web.v1.payload.AuthenticationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthApi {

    private final AuthHelper authHelper;

    @Autowired
    public AuthApi(AuthHelper authHelper) {
        this.authHelper = authHelper;
    }

    /**
     * Authenticates a user and returns an authentication response.
     *
     * @param authenticationRequest the authentication request containing email and password
     * @param bindingResult         the binding result to hold validation errors
     * @return a response data object containing the authentication response
     */
    @PostMapping("/authenticate")
    @Operation(
            summary = "Authenticate a user",
            description = "Authenticates a user based on the provided credentials and returns an authentication response.",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully authenticated",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content = @Content
                    ),
            }
    )
    public ResponseData<AuthResponse> login(
            @RequestBody AuthenticationRequest authenticationRequest,
            BindingResult bindingResult
    ) {
        return authHelper.authenticate(authenticationRequest, bindingResult);
    }
}
