package com.client.controller;

import com.client.model.dto.LoginDto;
import com.client.model.dto.UserRegistrationDto;
import com.client.security.ClientDetailsService;
import com.client.service.BasicServiceImpl;
import com.client.service.GoogleAuthServiceImpl;
import com.client.service.LoginServiceImpl;
import com.client.service.RegisterServiceImpl;
import com.client.util.ExternalAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.client.util.UrlConstants.EDITORIAL_REGISTRATION_URL;
import static com.client.util.UrlConstants.GOOGLE_AUTHORIZATION_ENDPOINT;

@RestController
@RequestMapping("/client")
public class LoginController {

    private final LoginServiceImpl loginService;
    private final RegisterServiceImpl registerService;
    private final ClientDetailsService clientDetailsService;
    private final BasicServiceImpl basicService;
    private final AuthenticationManager authenticationManager;
    private final GoogleAuthServiceImpl googleAuthService;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    public LoginController(LoginServiceImpl loginService, RegisterServiceImpl registerService, ClientDetailsService clientDetailsService, BasicServiceImpl basicService, AuthenticationManager authenticationManager, GoogleAuthServiceImpl googleAuthService) {
        this.loginService = loginService;
        this.registerService = registerService;
        this.clientDetailsService = clientDetailsService;
        this.basicService = basicService;
        this.authenticationManager = authenticationManager;
        this.googleAuthService = googleAuthService;
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
            loginService.setUserSession(request, response, null, loginDto);
            return ResponseEntity.ok("Login successful!");
        } catch (AuthenticationException e) {
            if (e.getCause() instanceof ExternalAuthenticationException a)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You cannot log in this way. You must log in via " + a.getMessage());
            else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password!");
        }
    }

    @GetMapping("/login/google")
    public ResponseEntity<Object> loginGoogle() {
        String googleAuthUrl = GOOGLE_AUTHORIZATION_ENDPOINT
                + "?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&scope=openid%20email%20profile";
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(googleAuthUrl)).build();
    }

    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<String> getAuthorizationGoogle(@RequestParam("code") String code, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        String responseBody = googleAuthService.getTokenResponseBody(restTemplate, code, clientId, redirectUri, clientSecret);
        if (responseBody == null) {
            return ResponseEntity.badRequest().body("Unsuccessful authentication via google account.");
        }

        String accessToken = googleAuthService.getAccessToken(responseBody);
        ResponseEntity<String> accessResponse = googleAuthService.getUserInfoResponse(restTemplate, accessToken);

        JSONObject jsonObject = new JSONObject(accessResponse.getBody());
        String email = jsonObject.getString("email");
        UserRegistrationDto userRegistrationDto = UserRegistrationDto.jsonToDto(jsonObject);
        if (!loginService.checkIfUserExistsByEmail(email)) {
            registerService.registerUser(userRegistrationDto);
            restTemplate.postForEntity(EDITORIAL_REGISTRATION_URL, userRegistrationDto, String.class);
        }

        loginService.setUserSession(httpServletRequest, httpServletResponse, userRegistrationDto, null);

        return ResponseEntity.ok("Successful login via google account.");
    }

    @GetMapping("/test")
    public ResponseEntity<String> testMessageAfterLogin() {
        return ResponseEntity.ok("WELCOME");
    }
}
