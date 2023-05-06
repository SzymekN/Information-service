package com.editorial.service;

import com.editorial.model.dto.LoginDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final BasicServiceImpl basicService;

    @Autowired
    public LoginServiceImpl(AuthenticationManager authenticationManager, BasicServiceImpl basicService) {
        this.authenticationManager = authenticationManager;
        this.basicService = basicService;
    }

    @Override
    public void setUserSession(HttpServletRequest request, HttpServletResponse response, LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        basicService.roleCookieCreation(request, response);
        HttpSession session = request.getSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }
}
