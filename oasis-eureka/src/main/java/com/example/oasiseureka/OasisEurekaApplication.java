package com.example.oasiseureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class OasisEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(OasisEurekaApplication.class, args);
	}

}
