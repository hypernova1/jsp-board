package com.spring;

import com.spring.core.BeanContainer;
import com.spring.core.BeanLoader;
import com.spring.core.DependencyManager;

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
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
