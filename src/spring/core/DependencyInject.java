package spring.core;

import spring.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DependencyInject {

    private BeanLoader beanLoader = BeanLoader.getInstance();
    private BeanContainer beanContainer = BeanContainer.getInstance();

    private static DependencyInject instance;

    public static DependencyInject getInstance() {
        if (instance == null) instance = new DependencyInject();
        return instance;
    }

    public void execute() {

        Map<String, Class<?>> componentClasses = beanLoader.getComponentClasses();
        Map<String, Class<?>> controllerClasses = beanLoader.getControllerClasses();
        componentClasses.putAll(controllerClasses);

        Set<String> componentNames = componentClasses.keySet();

        for (String componentName : componentNames) {
            Field[] declaredFields = componentClasses.get(componentName).getDeclaredFields();

            for (Field field : declaredFields) {
                if (Objects.nonNull(field.getAnnotation(Autowired.class))) {
                    field.setAccessible(true);
                    try {
                        String beanName = field.getType().getSimpleName();
                        beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
                        field.set(beanContainer.getBean(componentName), beanContainer.getBean(beanName));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }

}
