package com.client.controller;

import com.client.model.dto.LoginDto;
import com.client.model.dto.UserRegistrationDto;
import com.client.security.ClientDetailsService;
import com.client.service.BasicServiceImpl;
import com.client.service.LoginServiceImpl;
import com.client.service.RegisterServiceImpl;
import com.client.util.ExternalAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.client.util.Constants.*;

@RestController
@RequestMapping("/client")
public class LoginController {

    private final LoginServiceImpl loginService;
    private final RegisterServiceImpl registerService;
    private final ClientDetailsService clientDetailsService;
    private final BasicServiceImpl basicService;
    private final AuthenticationManager authenticationManager;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    public LoginController(LoginServiceImpl loginService, RegisterServiceImpl registerService, ClientDetailsService clientDetailsService, BasicServiceImpl basicService, AuthenticationManager authenticationManager) {
        this.loginService = loginService;
        this.registerService = registerService;
        this.clientDetailsService = clientDetailsService;
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
        String responseBody = getTokenResponseBody(restTemplate, code);
        if (responseBody == null) {
            return ResponseEntity.badRequest().body("Unsuccessful authentication via google account.");
        }

        String accessToken = getAccessToken(responseBody);
        ResponseEntity<String> accessResponse = getUserInfoResponse(restTemplate, accessToken);

        JSONObject jsonObject = new JSONObject(accessResponse.getBody());
        String email = jsonObject.getString("email");
        UserRegistrationDto userRegistrationDto = UserRegistrationDto.jsonToDto(jsonObject);
        if (!loginService.checkIfUserExistsByEmail(email)) {
            registerService.registerUser(userRegistrationDto);
            restTemplate.postForEntity(EDITORIAL_REGISTRATION_URL, userRegistrationDto, String.class);
        }

        UserDetails user = clientDetailsService.loadUserByUsernameOAuth2(userRegistrationDto);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        basicService.roleCookieCreation(httpServletRequest, httpServletResponse);
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        return ResponseEntity.ok("Successful login via google account.");
    }

    private String getTokenResponseBody(RestTemplate restTemplate, String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("redirect_uri", redirectUri);
        map.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(GOOGLE_TOKEN_ENDPOINT, request, String.class);
        return response.getBody();
    }

    private String getAccessToken(String responseBody) throws JSONException {
        String accessToken = "";
        JSONObject jsonObject = new JSONObject(responseBody);
        accessToken = jsonObject.getString("access_token");
        return accessToken;
    }

    private ResponseEntity<String> getUserInfoResponse(RestTemplate restTemplate, String accessToken) {
        HttpHeaders accessHeaders = new HttpHeaders();
        accessHeaders.setBearerAuth(accessToken);
        HttpEntity<String> accessEntity = new HttpEntity<>("", accessHeaders);
        return restTemplate.exchange(GOOGLE_USERINFO_ENDPOINT, HttpMethod.GET, accessEntity, String.class);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testMessageAfterLogin() {
        return ResponseEntity.ok("WELCOME");
    }
}
