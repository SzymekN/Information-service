package com.editorial.service;

import com.editorial.model.dto.LoginDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {
    void setUserSession(HttpServletRequest request, HttpServletResponse response, LoginDto loginDto);
}
