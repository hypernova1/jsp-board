package com.spring.core;

import com.spring.annotation.component.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BeanLoader {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Class<?>[] allClasses = new Class[0];
    private Map<String, Class<?>> controllerClasses = new HashMap<>();
    private Map<String, Class<?>> componentClasses = new HashMap<>();
    private Map<String, Class<?>> beanClasses = new HashMap<>();

    private static BeanLoader instance;

    private BeanLoader() {
        try {
            List<Class<?>> controllerType = Arrays.asList(Controller.class, RestController.class);
            List<Class<?>> componentTypes = Arrays.asList(Component.class, Service.class, Configuration.class);

            allClasses = ClassLoader.getClasses("com.board");
            this.controllerClasses = this.initClassBeans(controllerType);
            this.componentClasses = this.initClassBeans(componentTypes);
            this.beanClasses = this.initMethodBeans();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static BeanLoader getInstance() {
        if (instance == null) instance = new BeanLoader();
        return instance;
    }

    private Map<String, Class<?>> initClassBeans(List<Class<?>> componentTypes) {
        Map<String, Class<?>> result = new HashMap<>();
        for (Class<?> clazz : allClasses) {
            findAndCreateClassBeans(componentTypes, result, clazz);
        }
        return result;
    }

    private Map<String, Class<?>> initMethodBeans() {
        Map<String, Class<?>> result = new HashMap<>();

        Set<String> keys = componentClasses.keySet();
        for (String key : keys) {
            Method[] declaredMethods = componentClasses.get(key).getDeclaredMethods();
            for (Method method : declaredMethods) {
                findAndCreateMethodBeans(result, key, method);
            }
        }
        return result;
    }

    private void findAndCreateClassBeans(List<Class<?>> componentTypes, Map<String, Class<?>> result, Class<?> clazz) {
        Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();
        for (Annotation annotation : declaredAnnotations) {
            if (componentTypes.contains(annotation.annotationType())) {
                createClassBean(result, clazz);
            }
        }
    }

    private void findAndCreateMethodBeans(Map<String, Class<?>> result, String key, Method method) {
        Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
        for (Annotation annotation : declaredAnnotations) {
            if (annotation.annotationType().equals(Bean.class)) {
                createBean(result, key, method);
            }
        }
    }

    private void createClassBean(Map<String, Class<?>> result, Class<?> clazz) {
        String beanName = clazz.getSimpleName();
        beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
        result.put(beanName, clazz);
        logger.info("create " + beanName + " - " + clazz.getName());
    }

    private void createBean(Map<String, Class<?>> result, String key, Method method) {
        try {
            Object instance = method.invoke(componentClasses.get(key).newInstance());
            result.put(method.getName(), instance.getClass());
            logger.info("create " + method.getName() + " - " + method.getReturnType());
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Class<?>> getComponentClasses() {
        return this.componentClasses;
    }

    public Map<String, Class<?>> getControllerClasses() {
        return controllerClasses;
    }

    public Map<String, Class<?>> getBeanClasses() {
        return this.beanClasses;
    }

}
