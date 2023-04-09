package com.client.service;

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
        final int cookieDuration = 3600 * 24;
        final String cookiePath = "/";
        final String cookieName = "ROLE";

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        Cookie cookie = new Cookie(cookieName, Base64.getEncoder().encodeToString(role.getBytes()));
        cookie.setPath(cookiePath);
        cookie.setMaxAge(cookieDuration); // seconds provided (1 day)
        response.addCookie(cookie);
        System.out.println("Logged and redirected - getting cookie: " + cookie);
    }

    public void roleCookieRemoval(HttpServletRequest request, HttpServletResponse response) {
        final int cookieDuration = 0;
        final String cookiePath = "/";
        final String cookieName = "ROLE";

        Cookie cookie = new Cookie(cookieName, null);
        cookie.setPath(cookiePath);
        cookie.setMaxAge(cookieDuration);
        response.addCookie(cookie);
    }
}
