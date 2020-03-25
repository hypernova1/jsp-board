package spring;

import spring.core.BeanLoader;
import spring.core.DependencyInject;
import spring.core.BeanContainer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SpringListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("hello servlet!");
        BeanLoader.getInstance();
        BeanContainer.getInstance();
        DependencyInject.getInstance().execute();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
