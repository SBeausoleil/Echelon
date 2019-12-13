package com.sb.echelon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.sb.echelon.beans.AnalyzedClass;
import com.sb.echelon.beans.TestGeneratedIdBean;
import com.sb.echelon.test.ArraysBean;
import com.sb.echelon.test.Owner;
import com.sb.echelon.test.Possessed;

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

		private String[] hints;
	}

	@Autowired
	private Echelon echelon;

	@Bean("runner")
	public App app() {
		return new App();
	}

	private void run() {
		TestGeneratedIdBean bean = new TestGeneratedIdBean("Hello world!", 19);
		echelon.save(bean);
		final String TEXT = "A new world awaits us.";
		bean.setText(TEXT);
		echelon.save(bean);
		System.out.println(bean);
		TestGeneratedIdBean loaded = echelon.load(TestGeneratedIdBean.class, bean.getId());
		System.out.println(loaded);

		var nested = new NestedClass(1, 'f', "Bar", new String[] { "Hello", "World", "This is great" });
		System.out.println(echelon.save(nested));
		System.out.println(echelon.load(NestedClass.class, nested.getId()));

		var arrays = new ArraysBean(0l, new short[] { 4, 5, 12 }, new Short[] { 99 }, new boolean[] {true, true, false, true}, new char[] {'l', 'M', 'k', 'H', 'h', '.', 'X' } );
		System.out.println(echelon.save(arrays));
		System.out.println(echelon.load(ArraysBean.class, arrays.getId()));
		
		var owner = new Owner(10212.91f, new Possessed("Inventory"));
		System.out.println(echelon.save(owner));
		System.out.println(owner.getPossession());
		System.out.println(echelon.load(Owner.class, owner.getId()));
		
		
		AnalyzedClass<TestGeneratedIdBean> analyzed = (AnalyzedClass<TestGeneratedIdBean>) echelon.getAnalyzed(TestGeneratedIdBean.class);
		String sql = "SELECT * FROM " + analyzed.getTable() + " WHERE text = ?";
		TestGeneratedIdBean loadedFromRaw = echelon.loadRaw(sql, new Object[] { TEXT }, TestGeneratedIdBean.class).get(0);
		System.out.println(loadedFromRaw);
	}

	public static void main(String[] args) {
		var applicationContext = new AnnotationConfigApplicationContext(App.class);
		applicationContext.getBean("runner", App.class).run();
		applicationContext.close();
	}
}
