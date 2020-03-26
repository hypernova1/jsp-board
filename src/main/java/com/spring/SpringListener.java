package spring;

import spring.core.BeanContainer;
import spring.core.BeanLoader;
import spring.core.DependencyManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SpringListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("hello my spring!");
        BeanLoader.getInstance();
        BeanContainer.getInstance();
        DependencyManager.getInstance().injection();
    }
}
