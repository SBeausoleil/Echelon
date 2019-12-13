package com.sb.echelon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.sb.echelon.beans.TestGeneratedIdBean;

@Configuration
@ComponentScan
public class App {
	
	@Autowired
	private Echelon echelon;
	
	@Bean
	public App springMain() {
		TestGeneratedIdBean bean = new TestGeneratedIdBean("Hello world!");
		echelon.save(bean);
		bean.setText("A new world awaits us.");
		echelon.save(bean);
		return new App();
	}
	
	public static void main(String[] args) {
		var applicationContext = new AnnotationConfigApplicationContext(App.class);
		applicationContext.close();
	}
}
