package com.editorial.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Base64;
import java.util.Enumeration;

import static com.editorial.util.UrlConstants.EDITORIAL_LOGOUT_URL;

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

    public HttpHeaders copyHeadersFromRequest(HttpServletRequest request) {
        HttpHeaders headers = new org.springframework.http.HttpHeaders();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.set(headerName, request.getHeader(headerName));
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                headers.add("Cookie", cookie.getName() + "=" + cookie.getValue());
            }
        }

        return headers;
    }

    public ResponseEntity<Object> forceUserLogout() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(EDITORIAL_LOGOUT_URL));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
