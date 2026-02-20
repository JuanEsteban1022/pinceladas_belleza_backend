package com.management.backend_pinceladas_belleza.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public AuthenticationProvider testAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    @Primary
    public PasswordEncoder testPasswordEncoder() {
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }

    @Bean
    @Primary
    public UserDetailsService testUserDetailsService() {
        return username -> org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password("password")
                .roles("USER")
                .build();
    }
}
