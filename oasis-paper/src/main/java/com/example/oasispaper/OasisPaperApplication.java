package com.example.oasispaper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class OasisPaperApplication {

	public static void main(String[] args) {
		SpringApplication.run(OasisPaperApplication.class, args);
	}

}
