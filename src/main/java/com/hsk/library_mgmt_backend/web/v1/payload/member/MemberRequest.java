package com.hsk.library_mgmt_backend.web.v1.payload.member;

import com.hsk.library_mgmt_backend.web.v1.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

public record MemberRequest(
        @NotBlank(message = "Name is empty")
        @Size(max = 255, message = "Name must be less than 255 characters")
        String name,

        @NotBlank(message = "Email is empty")
        @Email(message = "Email should be valid")
        @Size(max = 255, message = "Email must be less than 255 characters")
        String email,

        @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number should be valid")
        String phone,

        @Enumerated(EnumType.STRING)
        @NotNull(message = "Role is empty")
        Role role,

        @NotBlank(message = "Password is empty")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password
) {
}
