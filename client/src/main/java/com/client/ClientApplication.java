package com.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
@EnableDiscoveryClient
@SpringBootApplication
@EnableRedisHttpSession
@RestController
public class ClientApplication {

	@Value("${eureka.instance.instance-id}")
	private String instanceId;

	@GetMapping("/client")
	public String idTest(){
		return "Client app of id: " + instanceId;
	}

	public static void main(String[] args) { SpringApplication.run(ClientApplication.class, args); }

}
