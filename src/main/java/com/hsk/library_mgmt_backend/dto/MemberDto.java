package com.hsk.library_mgmt_backend.dto;

import com.hsk.library_mgmt_backend.web.v1.enums.Role;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.hsk.library_mgmt_backend.persistent.entity.Member}
 */
public record MemberDto(
        Long id,

        LocalDateTime createdAt,

        String createdBy,

        LocalDateTime updatedAt,

        String updatedBy,
        String name,
        String email,
        String phone,
        Role role,
        String password
) implements Serializable {
}