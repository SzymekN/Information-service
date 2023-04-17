package com.client.controller;

import com.client.service.BasicServiceImpl;
import com.client.service.LoginServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/client")
public class LoginController {

    private static final String HOME_PAGE = "/client/home";
    private static final String REGISTRATION_PAGE = "/client/final-page-registration";
    private final LoginServiceImpl loginService;
    private final BasicServiceImpl basicService;

    @Autowired
    public LoginController(LoginServiceImpl loginService, BasicServiceImpl basicService) {
        this.loginService = loginService;
        this.basicService = basicService;
    }

    @Value("${eureka.instance.instance-id}")
    private String instanceId;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @GetMapping("/login")
    public String idTest() {
        return "login";
    }

    @GetMapping("/login/google")
    public ResponseEntity<Object> loginGoogle() {
        String googleAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&scope=openid%20email%20profile";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create(googleAuthUrl));
        System.out.println(googleAuthUrl);
//        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(googleAuthUrl)).build();
    }

    @GetMapping("/test2")
    public ResponseEntity<Object> getAuthorizationGoogle(@RequestParam("code") String code) throws JSONException {

        System.out.println(code);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("redirect_uri", redirectUri);
        map.add("grant_type", "authorization_code");

        String tokenEndpoint = "https://oauth2.googleapis.com/token";

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(tokenEndpoint, request, String.class);
        String responseBody = response.getBody();

        String accessToken = "";
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            accessToken = jsonObject.getString("access_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(accessToken);

        HttpHeaders accessHeaders = new HttpHeaders();
        accessHeaders.setBearerAuth(accessToken);
        HttpEntity<String> accessEntity = new HttpEntity<>("", accessHeaders);
        String userInfoEndpoint = "https://www.googleapis.com/oauth2/v1/userinfo";
        ResponseEntity<String> accessResponse = restTemplate.exchange(userInfoEndpoint, HttpMethod.GET, accessEntity, String.class);
        String accessResponseString = accessResponse.getBody();

        System.out.println(accessResponseString);

//        if (response.getStatusCode() == HttpStatus.OK) {
//            JSONObject jsonObject = new JSONObject(accessResponse.getBody());
//            String email = jsonObject.getString("email");
//            String givenName = jsonObject.getString("given_name");
//            String familyName = jsonObject.getString("family_name");
//            System.out.println(email + " " + givenName + " " + familyName);
//        }

        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonObject = new JSONObject(accessResponse.getBody());
            String email = jsonObject.getString("email");
            String givenName = jsonObject.getString("given_name");
            String familyName = jsonObject.getString("family_name");
            System.out.println(email + " " + givenName + " " + familyName);
            if(loginService.checkIfUserExistsByEmail(email))
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(HOME_PAGE)).build();
            else
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(REGISTRATION_PAGE)).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).location(URI.create("/client/error")).build();
    }

    @GetMapping("/home")
    public String getHomePage(){
        return "Successful login!";
    }

    @GetMapping("/final-page-registration")
    public ResponseEntity<String> getFinalRegistrationPage(){
        return ResponseEntity.ok("Fill out the registration form!");
    }

    @RequestMapping(value="/logout", method= {RequestMethod.GET, RequestMethod.POST})
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
            basicService.roleCookieRemoval(request, response);
            System.out.println("IN LOGOUT - delete handler");
        }
        return "redirect:/client/login?logout";
    }

    @GetMapping("/test")
    public String testIdAfterLogin(Model model, HttpServletRequest request, HttpServletResponse response) {
        basicService.roleCookieCreation(request, response);
        model.addAttribute("instanceId", "ID: " + instanceId);
        return "test-id";
    }

}
