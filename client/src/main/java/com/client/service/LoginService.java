package com.client.service;

import com.client.model.dto.LoginDto;
import com.client.model.dto.UserRegistrationDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    boolean checkIfUserExistsByEmail(String email);
    ResponseEntity<Object> setUserSession(HttpServletRequest request, HttpServletResponse response, UserRegistrationDto userRegistrationDto, LoginDto loginDto);
}
