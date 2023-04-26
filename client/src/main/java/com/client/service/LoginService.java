package com.client.service;

import com.client.model.dto.LoginDto;
import com.client.model.dto.UserRegistrationDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {
    boolean checkIfUserExistsByEmail(String email);
    void setUserSession(HttpServletRequest request, HttpServletResponse response, UserRegistrationDto userRegistrationDto, LoginDto loginDto);
}
