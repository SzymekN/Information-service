package com.client;

import com.client.service.BasicServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@EnableDiscoveryClient
@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60)
@Controller
@RequestMapping("/zamiana")
public class ClientApplication {

	@Value("${eureka.instance.instance-id}")
	private String instanceId;
	@Autowired
	private BasicServiceImpl basicService;

	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;
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
	public String loginGoogle() {
		String googleAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth"
				+ "?response_type=code"
				+ "&client_id=" + clientId
				+ "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
				+ "&scope=openid%20email%20profile";
		System.out.println(googleAuthUrl);
		return "redirect:" + googleAuthUrl;
	}

	@GetMapping("/test2")
	public String testIfWorks(@RequestParam("code") String code) throws JSONException {

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

		if (response.getStatusCode() == HttpStatus.OK) {
			JSONObject jsonObject = new JSONObject(accessResponse.getBody());
			String email = jsonObject.getString("email");
			String givenName = jsonObject.getString("given_name");
			String familyName = jsonObject.getString("family_name");
			System.out.println(email + " " + givenName + " " + familyName);
		}

		return "redirect:/client/test";
	}

	@RequestMapping(value="/logout", method= {RequestMethod.GET, RequestMethod.POST})
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
			new SecurityContextLogoutHandler().logout(request, response, auth);;
			basicService.roleCookieRemoval(request, response);
			System.out.println("IN LOGOUT - delete handler");
		}
		return "redirect:/client/login?logout";
	}

	@GetMapping("/test")
	public String testIdAfterLogin(Model model, HttpServletRequest request, HttpServletResponse response) {
		//basicService.roleCookieCreation(request, response);
		model.addAttribute("instanceId", "ID: " + instanceId);
		return "test-id";
	}


	public static void main(String[] args) { SpringApplication.run(ClientApplication.class, args); }

}
