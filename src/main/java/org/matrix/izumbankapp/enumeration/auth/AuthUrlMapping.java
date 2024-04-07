package org.matrix.izumbankapp.enumeration.auth;

import lombok.Getter;

@Getter
public enum AuthUrlMapping {
    MANAGER(Role.MANAGER.name(), new String[]{
            "/api/v1/accounts/{accountNumber}/status/{status}",
            "/api/v1/accounts/search",
            "/api/v1/accounts/{id}",
            "/api/v1/accounts",
            "/api/v1/support/**",
            "/api/v1/currency/file",
            "/api/v1/notifications/**",
            "/api/v1/users/**"
    }),

    ADMIN(Role.ADMIN.name(), new String[]{
            "/api/v1/transactions/**",
            "/api/v1/operations/deposit-scheduler",
            "/api/v1/currency",
            "/api/v1/exchange/**",
    }),
    PERMIT_ALL(null, new String[]{
            "/api/v1/auth/authenticate",
            "/api/v1/auth/register",
            "/api/v1/support/request",
            "/api/v1/exchange/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/swagger-ui/**",
            "/swagger-ui.html"
    }),

    ANY_AUTHENTICATED(null, new String[]{
            "/api/v1/auth/change-password",
            "/api/v1/operations/transfer",
            "/api/v1/operations/withdrawal",
            "/api/v1/operations/deposit",
            "/api/v1/operations/{accountNumber}/balance"
    });

    private final String role;
    private final String[] urls;

    AuthUrlMapping(String role, String[] urls) {
        this.role = role;
        this.urls = urls;
    }

}
