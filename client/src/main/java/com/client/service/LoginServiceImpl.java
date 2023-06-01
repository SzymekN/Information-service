package com.client.service;

import com.client.model.dto.AuthenticatedUserDto;
import com.client.model.dto.LoginDto;
import com.client.model.dto.UserRegistrationDto;
import com.client.model.entity.User;
import com.client.repository.UserRepository;
import com.client.security.ClientDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> setUserSession(HttpServletRequest request, HttpServletResponse response, UserRegistrationDto userRegistrationDto, LoginDto loginDto) {
        Authentication authentication = null;
        AuthenticatedUserDto authenticatedUserDto = null;
        if (userRegistrationDto != null && GOOGLE_SUPPLIER.equals(userRegistrationDto.getSupplier())) {
            UserDetails user = clientDetailsService.loadUserByUsernameOAuth2(userRegistrationDto);
            authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            User userFromDb = userRepository.findUserByName(userRegistrationDto.getUsername());
            authenticatedUserDto = userToAuthenticationDto(userFromDb);
        } else if (loginDto != null) {
            User userFromDb = userRepository.findUserByName(loginDto.getUsername());
            if (userFromDb != null && !userFromDb.getEnabled()) return new ResponseEntity<>("Account has not been activated yet!", HttpStatus.FORBIDDEN);
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            authenticatedUserDto = userToAuthenticationDto(userFromDb);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        basicService.roleCookieCreation(request, response);
        HttpSession session = request.getSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
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
