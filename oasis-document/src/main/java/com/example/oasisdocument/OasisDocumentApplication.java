package com.example.oasisdocument;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableEurekaClient
@EnableRedisHttpSession
public class OasisDocumentApplication {

	public static void main(String[] args) {
		SpringApplication.run(OasisDocumentApplication.class, args);
	}

}
