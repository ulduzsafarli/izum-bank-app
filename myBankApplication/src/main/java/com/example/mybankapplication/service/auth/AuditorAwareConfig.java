package com.example.mybankapplication.service.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;


@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@Configuration
public class AuditorAwareConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> java.util.Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
