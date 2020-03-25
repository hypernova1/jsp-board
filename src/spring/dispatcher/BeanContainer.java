package spring.dispatcher;

import spring.annotation.component.Component;
import spring.core.BeanLoader;
import spring.exception.BeanNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class BeanContainer {

    private Map<String, Object> beanMap = new HashMap<>();

    private static BeanContainer instance;

    private BeanContainer() {
        this.setBeanMap();
    }

    public static BeanContainer getInstance() {
        if (instance == null) instance = new BeanContainer();
        return instance;
    }

    public Object getBean(String beanName) {
        Object bean = beanMap.get(beanName);
        if (bean == null) throw new BeanNotFoundException(beanName);
        return bean;
    }

    private void setBeanMap() {
        BeanLoader beanLoader = BeanLoader.getInstance();
        List<Class<?>> componentClasses = beanLoader.getComponentClasses();

        for (Class<?> componentClass : componentClasses) {
            try {
                String beanName = componentClass.getSimpleName();
                beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
                Object beanInstance = componentClass.newInstance();
                beanMap.put(beanName, beanInstance);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        Map<String, Object> beanMethods = beanLoader.getBeanInstances();
        Set<String> keys = beanMethods.keySet();
        for (String key : keys) {
            beanMap.put(key, beanMethods.get(key));
        }

    }

}
