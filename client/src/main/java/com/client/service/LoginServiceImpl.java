package com.client.service;

import com.client.model.dto.LoginDto;
import com.client.model.dto.UserRegistrationDto;
import com.client.repository.UserRepository;
import com.client.security.ClientDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import static com.client.util.AccountConstants.GOOGLE_SUPPLIER;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final ClientDetailsService clientDetailsService;
    private final BasicServiceImpl basicService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginServiceImpl(UserRepository userRepository, ClientDetailsService clientDetailsService, BasicServiceImpl basicService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.clientDetailsService = clientDetailsService;
        this.basicService = basicService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean checkIfUserExistsByEmail(String email) {
        return userRepository.existsUsersByEmail(email);
    }

    @Override
    public void setUserSession(HttpServletRequest request, HttpServletResponse response, UserRegistrationDto userRegistrationDto, LoginDto loginDto) {
        Authentication authentication = null;
        if (userRegistrationDto != null && GOOGLE_SUPPLIER.equals(userRegistrationDto.getSupplier())) {
            UserDetails user = clientDetailsService.loadUserByUsernameOAuth2(userRegistrationDto);
            authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        } else if (loginDto != null) {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        basicService.roleCookieCreation(request, response);
        HttpSession session = request.getSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }
}
