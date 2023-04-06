package com.client;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Base64;

@EnableDiscoveryClient
@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60)
@Controller
@RequestMapping("/client")
public class ClientApplication {

	@Value("${eureka.instance.instance-id}")
	private String instanceId;

	@GetMapping("/login")
	public String idTest() {;
		return "login";
	}

	@RequestMapping(value="/logout", method= {RequestMethod.GET, RequestMethod.POST})
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
			new SecurityContextLogoutHandler().logout(request, response, auth);;
			Cookie cookie = new Cookie("ROLE", null);
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
			System.out.println("IN LOGOUT - delete handler");
		}
		return "redirect:/client/login?logout";
	}

	@GetMapping("/test")
	public String testIdAfterLogin(Model model, HttpServletRequest request, HttpServletResponse response){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String role = userDetails.getAuthorities().iterator().next().getAuthority();
		Cookie cookie = new Cookie("ROLE", Base64.getEncoder().encodeToString(role.getBytes()));
		cookie.setPath("/");
		cookie.setMaxAge(3600 * 24); // seconds provided (1 day)
		response.addCookie(cookie);
		model.addAttribute("instanceId", "ID: " + instanceId);
		System.out.println("Logged and redirected - getting cookie: " + cookie);
		return "test-id";
	}

	public static void main(String[] args) { SpringApplication.run(ClientApplication.class, args); }

}
