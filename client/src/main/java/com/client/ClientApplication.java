package com.client;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
@EnableDiscoveryClient
@SpringBootApplication
@EnableRedisHttpSession
@Controller
@RequestMapping("/client")
public class ClientApplication {

	@Value("${eureka.instance.instance-id}")
	private String instanceId;

	@GetMapping("/login")
	public String idTest(){
		return "login";
	}

	@RequestMapping(value="/logout", method= RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/client/login";
	}

	@GetMapping("/test")
	public String testIdAfterLogin(Model model){
		model.addAttribute("instanceId", "ID: " + instanceId);
		return "test-id";
	}

	public static void main(String[] args) { SpringApplication.run(ClientApplication.class, args); }

}
