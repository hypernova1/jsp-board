package spring;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SpringListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("hello servlet!");

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
