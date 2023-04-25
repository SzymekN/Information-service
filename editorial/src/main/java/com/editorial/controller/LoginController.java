package com.editorial.controller;

import com.editorial.model.dto.LoginDto;
import com.editorial.service.BasicServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/editorial")
public class LoginController {

    private final BasicServiceImpl basicService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginController(BasicServiceImpl basicService, AuthenticationManager authenticationManager) {
        this.basicService = basicService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public ResponseEntity<String> showLogoutMessage(@RequestParam(name = "logout", required = false) String logout) {
        if (logout != null) {
            return ResponseEntity.ok("You have been successfully logged out.");
        }
        return ResponseEntity.ok("Login page.");
    }

    @PostMapping("/login2")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            basicService.roleCookieCreation(request, response);

            HttpSession session = request.getSession();
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            return ResponseEntity.ok("Login successful.");
        } catch (AuthenticationException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password!");
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> testMessageAfterLogin() {
        return ResponseEntity.ok("WELCOME");
    }
}
