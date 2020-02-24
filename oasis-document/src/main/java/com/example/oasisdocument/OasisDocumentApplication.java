package com.example.oasisdocument;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class OasisDocumentApplication {

	public static void main(String[] args) {
		SpringApplication.run(OasisDocumentApplication.class, args);
	}

}
