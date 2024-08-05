package com.hsk.library_mgmt_backend.persistent.entity.base;


import com.hsk.library_mgmt_backend.persistent.entity.RevInfo;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class CustomRevisionListener implements RevisionListener {

    private static final String CURRENT_AUDITOR = "system";

    @Override
    public void newRevision(Object revisionEntity) {
        if (revisionEntity instanceof RevInfo revInfo) {

            String userType;
            String username = CURRENT_AUDITOR;

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication);
            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                username = authentication.getName();
                userType = String.valueOf(authentication.getAuthorities().stream().filter(role -> role.getAuthority().startsWith("ROLE_")).findFirst().get());
            } else {
                userType = "OFFICER";
            }
            revInfo.setUsername(username);
            revInfo.setUserType(userType);
        }
    }
}

