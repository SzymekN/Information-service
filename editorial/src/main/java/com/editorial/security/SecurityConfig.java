package com.editorial.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain web(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(configurer ->
                        configurer.requestMatchers("/").permitAll()
                                .requestMatchers("/editorial/**").permitAll()
                                .anyRequest().authenticated())
                .formLogin(configurer -> configurer.loginPage("/editorial/login")
                        .loginProcessingUrl("/authenticate")
                        .permitAll()
                        .defaultSuccessUrl("/editorial/test", true))
                .logout(configurer -> configurer.permitAll()
                        .logoutSuccessUrl("/editorial/login?logout"))
                .exceptionHandling(configurer->configurer.accessDeniedPage("/editorial/denied"))
                .build();
    }
}
