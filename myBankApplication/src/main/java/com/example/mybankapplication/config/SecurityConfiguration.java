package com.example.mybankapplication.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/api/v1/support",
            "/api/v1/support/respond", //admin
            "/api/v1/support/all-requests", //admin
            "/api/v1/support/all-unanswered-requests", //admin
            "/api/v1/exchange/fetch-currencies", //admin
            "/api/v1/exchange/exchange-from-AZN", //USER
            "/api/v1/exchange/exchange-to-AZN", //USER
            "/api/v1/exchange/currency-file", //admin
            "/api/v1/user/{userId}/accounts", //user

    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL).permitAll()
                                .anyRequest().authenticated()

//                                .requestMatchers(GET,"/users/search/{id}").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
//                                .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())
//                                .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
//                                .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
//                                .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
//                                .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())
//                                .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .logout(logout ->
//                        logout.logoutUrl("/api/v1/auth/logout")
//                                .addLogoutHandler(logoutHandler)
//                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
//                )
        ;

//        http.csrf(AbstractHttpConfigurer::disable)
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(AuthUrlMapping.PERMIT_ALL.getUrls()).permitAll()
//                        .requestMatchers(AuthUrlMapping.ADMIN.getUrls()).hasAnyAuthority(ADMIN.name())
//                        .requestMatchers(AuthUrlMapping.USER.getUrls()).hasAnyAuthority(USER.name())
//                        .requestMatchers(AuthUrlMapping.MANAGER.getUrls()).hasAnyAuthority(MANAGER.name())
//                        .requestMatchers(AuthUrlMapping.ANY_AUTHENTICATED.getUrls()).authenticated()
//                )
//                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .authenticationEntryPoint((request, response, authException) ->
//                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED)
//                        )
//                        .accessDeniedHandler((request, response, accessDeniedException) ->
//                                response.setStatus(HttpServletResponse.SC_FORBIDDEN)
//                        )
//                );

        return http.build();
    }
}
//                .httpBasic(Customizer.withDefaults()).logout(lg -> lg
//                        .logoutUrl("/api/v1/auth/logout")
//                        .addLogoutHandler(logoutHandler)
//                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()
//                        )
//                )



//    }
//}
//authorize -> authorize
//        .requestMatchers(AuthUrlMapping.PERMIT_ALL.getUrls()).permitAll()
//                                .requestMatchers(AuthUrlMapping.ADMIN.getUrls()).hasAnyAuthority(ROLE.ROLE_ADMIN.name())
//        .requestMatchers(AuthUrlMapping.CLIENT.getUrls()).hasAnyAuthority(ROLE.ROLE_CLIENT.name())
//        .requestMatchers(AuthUrlMapping.ANY_AUTHENTICATED.getUrls()).authenticated()
//                ).