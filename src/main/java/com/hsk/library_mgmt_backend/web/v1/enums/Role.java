package com.hsk.library_mgmt_backend.web.v1.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hsk.library_mgmt_backend.web.v1.enums.Permission.*;


@RequiredArgsConstructor
public enum Role {
    ADMIN(Set.of(ADMIN_READ, ADMIN_UPDATE, ADMIN_CREATE, ADMIN_DELETE, MEMBER_READ, MEMBER_UPDATE, MEMBER_CREATE, MEMBER_DELETE)),
    STUDENT(Set.of(MEMBER_READ, MEMBER_UPDATE, MEMBER_CREATE, MEMBER_DELETE)),
    FACULTY(Set.of(MEMBER_READ, MEMBER_UPDATE, MEMBER_CREATE, MEMBER_DELETE));

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
