package com.editorial.service;

import com.editorial.model.dto.AuthenticatedUserDto;
import com.editorial.model.dto.LoginDto;
import com.editorial.model.entity.User;
import com.editorial.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private final UserRepository userRepository;

    @Autowired
    public LoginServiceImpl(AuthenticationManager authenticationManager, BasicServiceImpl basicService,
                            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.basicService = basicService;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<Object> setUserSession(HttpServletRequest request, HttpServletResponse response, LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        basicService.roleCookieCreation(request, response);
        HttpSession session = request.getSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        User userFromDb = userRepository.findUserByName(loginDto.getUsername());
        AuthenticatedUserDto authenticatedUserDto = userToAuthenticationDto(userFromDb);
        return ResponseEntity.ok().body(authenticatedUserDto);
    }

    public AuthenticatedUserDto userToAuthenticationDto(User user) {
        return AuthenticatedUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getUserDetails().getName())
                .surname(user.getUserDetails().getSurname())
                .email(user.getUserDetails().getEmail())
                .supplier(user.getUserDetails().getSupplier())
                .build();
    }
}
