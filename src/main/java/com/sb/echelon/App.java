package com.sb.echelon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.sb.echelon.beans.TestBean;

@Configuration
@ComponentScan
public class App {
	private static ApplicationContext applicationContext;
	
	@Autowired
	private Echelon echelon;
	
	@Bean
	public App springMain() {
		System.out.println("In springMain()");
		System.out.println(echelon.analyze(TestBean.class));
		return new App();
	}
	
	public static void main(String[] args) {
		System.out.println("Starting");
		applicationContext = new AnnotationConfigApplicationContext(App.class);
		
		for (String beanName : applicationContext.getBeanDefinitionNames()) {
            System.out.println(beanName);
        }
	}
}
