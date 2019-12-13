package com.sb.echelon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.sb.echelon.beans.TestGeneratedIdBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@ComponentScan
public class App {
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Table
	public static class NestedClass {
		private long id;
		private char foo;
		private String bar;
	}
	
	@Autowired
	private Echelon echelon;
	
	@Bean("runner")
	public App app() {
		return new App();
	}
	
	private void run() {
		/*TestGeneratedIdBean bean = new TestGeneratedIdBean("Hello world!", 19);
		echelon.save(bean);
		bean.setText("A new world awaits us.");
		echelon.save(bean);
		System.out.println(bean);
		TestGeneratedIdBean loaded = echelon.load(TestGeneratedIdBean.class, bean.getId());
		System.out.println(loaded);*/
		
		var nested = new NestedClass(1, 'f', "Bar");
		System.out.println(echelon.save(nested));
		System.out.println(echelon.load(NestedClass.class, nested.getId()));
	}
	
	public static void main(String[] args) {
		var applicationContext = new AnnotationConfigApplicationContext(App.class);
		applicationContext.getBean("runner", App.class).run();
		applicationContext.close();
	}
}
