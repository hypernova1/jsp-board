package sam.spring;

import sam.spring.core.BeanContainer;
import sam.spring.core.BeanLoader;
import sam.spring.core.DependencyManager;
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
        logger.info("hello sam's pring!");
        BeanLoader.getInstance();
        BeanContainer.getInstance();
        DependencyManager.getInstance().injection();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
