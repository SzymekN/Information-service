package com.client.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new ClientDetailsService();
    }

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
                        .requestMatchers("/client/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(configurer -> configurer.loginPage("/client/login")
                        .loginProcessingUrl("/authenticate")
                        .permitAll()
                        .defaultSuccessUrl("/client/test", true))
                .logout(configurer -> configurer.permitAll()
                        .logoutSuccessUrl("/client/login?logout"))
                .exceptionHandling(configurer->configurer.accessDeniedPage("/client/denied"))
                .build();
    }
}
