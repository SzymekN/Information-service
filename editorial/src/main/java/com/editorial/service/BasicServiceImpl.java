package com.editorial.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class BasicServiceImpl {

    public void roleCookieCreation(HttpServletRequest request, HttpServletResponse response) {
        final int COOKIE_DURATION = 3600 * 24;
        final String COOKIE_PATH = "/";
        final String COOKIE_NAME = "ROLE";

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        Cookie cookie = new Cookie(COOKIE_NAME, Base64.getEncoder().encodeToString(role.getBytes()));
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(COOKIE_DURATION); // seconds provided (1 day)
        response.addCookie(cookie);
        System.out.println("Logged and redirected - getting cookie: " + cookie);
    }

    public void roleCookieRemoval(HttpServletRequest request, HttpServletResponse response) {
        final int COOKIE_DURATION = 0;
        final String COOKIE_PATH = "/";
        final String COOKIE_NAME = "ROLE";

        Cookie cookie = new Cookie(COOKIE_NAME, null);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(COOKIE_DURATION);
        response.addCookie(cookie);
    }
}
