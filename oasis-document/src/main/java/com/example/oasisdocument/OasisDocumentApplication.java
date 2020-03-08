package com.example.oasisdocument;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
@EnableAsync
@EnableCaching
public class OasisDocumentApplication {

	public static void main(String[] args) {
		SpringApplication.run(OasisDocumentApplication.class, args);
	}

	@Bean
	public TomcatServletWebServerFactory tomcatServletWebServerFactory() {

		// 修改内置的 tomcat 容器配置
		TomcatServletWebServerFactory tomcatServlet = new TomcatServletWebServerFactory();
		tomcatServlet.addConnectorCustomizers(
				(TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "[]{},")
		);

		return tomcatServlet;
	}
}
