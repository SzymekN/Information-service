package com.client.security;

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
        return http.cors().and().authorizeHttpRequests(configurer ->
                        configurer.requestMatchers("/").permitAll()
                                .requestMatchers("/client/login").permitAll()
                                .requestMatchers("/client/login/v2").permitAll()
                                .requestMatchers("/client/login/google").permitAll()
                                .requestMatchers("/client/login/oauth2/code/google").permitAll()
                                .requestMatchers("/client/registration").permitAll()
                                .requestMatchers("/client/registration/fe").permitAll()
                                .requestMatchers("/client/actions/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/client/test").hasRole("USER")
                                .requestMatchers("/client/articles/**").permitAll()
                                .anyRequest().authenticated())
                .formLogin(configurer -> configurer.loginPage("/client/login"))
                .logout(configurer -> configurer
                        .logoutUrl("/client/logout")
                        .logoutSuccessUrl("/client/login?logout")
                        //.invalidateHttpSession(true)
                        .deleteCookies("ROLE")
                        .permitAll())
                //.invalidateHttpSession(true)
                //.deleteCookies("SESSION"))
                .exceptionHandling(configurer -> configurer.accessDeniedPage("/client/denied"))
                .csrf().disable()
                .build();
    }
}