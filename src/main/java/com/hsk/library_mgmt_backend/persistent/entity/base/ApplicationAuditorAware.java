package com.hsk.library_mgmt_backend.persistent.entity.base;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;


@EqualsAndHashCode
@Slf4j
public class ApplicationAuditorAware implements AuditorAware<String> {

    private static final String CURRENT_AUDITOR = "system";

    @NotNull
    @Override
    public Optional<String> getCurrentAuditor() {

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication instanceof JwtAuthenticationToken jwt) {
//            final String username = jwt.getName();
//            final var roles = jwt.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
//            final var exp = jwt.getToken().getExpiresAt();
//            log.debug("roles : {}", roles);
//            log.debug("exp: {}", exp);
//            return Optional.of(username);
//        }
        return Optional.of(CURRENT_AUDITOR);
    }
}
