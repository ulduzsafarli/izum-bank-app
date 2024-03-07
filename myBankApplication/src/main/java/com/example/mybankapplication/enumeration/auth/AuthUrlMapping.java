package com.example.mybankapplication.enumeration.auth;

import lombok.Getter;

@Getter
public enum AuthUrlMapping {
    USER(Role.USER.name(), new String[] {
            "/client/**",
            "/branch/all",
            "/branch/{id}",
            "/account/**"
    }),

    MANAGER(Role.MANAGER.name(), new String[] {
            "/manager/**"
    }),

    ADMIN(Role.ADMIN.name(), new String[] {
            "/account/**",
            "/branch/**",
            "/client/**",
            "/auth/**"
    }),
    PERMIT_ALL(null, new String[] {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/auth/**",
            "/client/register",
            "/client/{id}",
    }),

    ANY_AUTHENTICATED(null, new String[] {

    });


    private final String role;
    private final String[] urls;

    AuthUrlMapping(String role, String[] urls) {
        this.role = role;
        this.urls = urls;
    }

}
