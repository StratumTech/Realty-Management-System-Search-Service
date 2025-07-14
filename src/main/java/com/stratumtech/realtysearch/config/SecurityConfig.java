package com.stratumtech.realtysearch.config;

import org.springframework.http.HttpMethod;

import static jakarta.servlet.DispatcherType.ERROR;
import static jakarta.servlet.DispatcherType.FORWARD;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .dispatcherTypeMatchers(FORWARD, ERROR).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/properties/**").permitAll()
                        .anyRequest().denyAll()
                ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}
