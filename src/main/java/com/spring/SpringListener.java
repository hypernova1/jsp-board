package com.spring;

import com.spring.core.BeanContainer;
import com.spring.core.BeanLoader;
import com.spring.core.DependencyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SpringListener implements ServletContextListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("hello my spring!");
        BeanLoader.getInstance();
        BeanContainer.getInstance();
        DependencyManager.getInstance().injection();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
