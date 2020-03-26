package spring;

import com.mysql.cj.util.StringUtils;
import spring.core.BeanLoader;
import spring.core.DependencyManager;
import spring.core.BeanContainer;

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

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
