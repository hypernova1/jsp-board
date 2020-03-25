package spring.core;

import spring.annotation.Autowired;
import spring.dispatcher.BeanContainer;

import java.lang.reflect.Field;
import java.util.Objects;

public class DependencyInject {

    private BeanLoader beanLoader = BeanLoader.getInstance();
    private BeanContainer beanContainer = BeanContainer.getInstance();

    private static DependencyInject instance;

    public static DependencyInject getInstance() {
        if (instance == null) instance = new DependencyInject();
        return instance;
    }

    public void execute() {
        Class<?>[] allClasses = beanLoader.getAllClasses();

        for (Class<?> clazz : allClasses) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (Objects.nonNull(field.getAnnotation(Autowired.class))) {
                    field.setAccessible(true);
                    try {
                        String componentName = clazz.getSimpleName();
                        componentName = componentName.substring(0, 1).toLowerCase() + componentName.substring(1);
                        System.out.println(componentName);
                        System.out.println(beanContainer.getBean(componentName));

                        field.set(beanContainer.getBean(componentName), beanContainer.getBean(field.getName()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
