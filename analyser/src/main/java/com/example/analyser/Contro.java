package com.example.analyser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Contro {
	@Value(value = "${name:python}")
	public String port;
	@Autowired
	private ConfigurableApplicationContext applicationContext;

	@GetMapping("/")
	public String hell() {
		return applicationContext.getEnvironment().getProperty("name") + ":" + port;
	}
}
