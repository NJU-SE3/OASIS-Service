package oasi.top.framework;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OasisFrame {
	public class Person implements BeanFactoryAware, BeanNameAware,
			InitializingBean, DisposableBean {
		@Override
		public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

		}

		@Override
		public void setBeanName(String s) {

		}

		@Override
		public void destroy() throws Exception {

		}

		@Override
		public void afterPropertiesSet() throws Exception {

		}
	}

	public static void main(String[] args) {
		SpringApplication.run(OasisFrame.class, args);
	}

}
