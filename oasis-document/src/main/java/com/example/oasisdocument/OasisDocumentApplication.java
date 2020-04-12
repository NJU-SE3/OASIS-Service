package com.example.oasisdocument;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableAsync
@EnableCaching
public class OasisDocumentApplication {

	public static void main(String[] args) {
		SpringApplication.run(OasisDocumentApplication.class, args);
	}

	@Bean
	public TomcatServletWebServerFactory tomcatServletWebServerFactory (){

		// 修改内置的 tomcat 容器配置
		TomcatServletWebServerFactory tomcatServlet = new TomcatServletWebServerFactory();
		tomcatServlet .addConnectorCustomizers(
				connector -> connector.setProperty("relaxedQueryChars", "[]{},")
		);
		return tomcatServlet ;
	}
}
