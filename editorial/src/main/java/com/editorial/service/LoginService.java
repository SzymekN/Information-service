package com.editorial.service;

import com.editorial.model.dto.LoginDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    ResponseEntity<Object> setUserSession(HttpServletRequest request, HttpServletResponse response, LoginDto loginDto);
}
