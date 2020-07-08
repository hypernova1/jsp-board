package com.spring.core;

import com.spring.annotation.Autowired;
import com.spring.exception.BeanNotFoundException;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DependencyManager {

    private BeanLoader beanLoader = BeanLoader.getInstance();
    private BeanContainer beanContainer = BeanContainer.getInstance();

    private static DependencyManager instance;

    public static DependencyManager getInstance() {
        if (instance == null) instance = new DependencyManager();
        return instance;
    }

    public void injection() {

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
                        boolean result = injectionToBeanName(componentName, field);
                        if (!result) throw new BeanNotFoundException(componentName);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean injectionToBeanName(String componentName, Field field) throws IllegalAccessException {
        String beanName = field.getType().getSimpleName();
        beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);

        Object bean = beanContainer.getBeanByName(beanName);
        if (!Objects.nonNull(bean)) {
            bean = injectionToBeanType(field.getType());
        }

        field.set(beanContainer.getBeanByName(componentName), bean);
        return true;
    }

    private Object injectionToBeanType(Class<?> fieldType) {
        Object bean = beanContainer.getBeanByType(fieldType);
        if (bean == null) {
            bean = injectionToBeanType(fieldType.getSuperclass());
        }
        return bean;
    }

}
