/**
 * 
 */
package org.isf.menu.manager;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Nanni
 *
 */
public class Context {
	
	private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
	
	/**
	 * Returns the main {@link ApplicationContext}. 
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
