package com.sb.echelon.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * A utility class that allows to access the Spring application anywhere at runtime.
 */
@Service
public class BeanUtil implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		BeanUtil.context = context;
	}

	/**
	 * Returns any bean managed by the application context.
	 * @param <T>
	 * @param beanClass
	 * @return
	 */
	public static <T> T getBean(Class<T> beanClass) {
		return context.getBean(beanClass);
	}
}