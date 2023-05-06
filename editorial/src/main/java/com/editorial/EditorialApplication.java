package com.editorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60)
@RestController
public class EditorialApplication {

	public static void main(String[] args) {
		SpringApplication.run(EditorialApplication.class, args);
	}
}
