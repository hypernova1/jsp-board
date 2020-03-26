package com.spring.core;

import com.spring.annotation.component.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BeanLoader {

    private final List<Class<?>> componentTypes = Arrays.asList(Component.class, Service.class, Configuration.class);
    private final List<Class<?>> controllerType = Arrays.asList(Controller.class, RestController.class);

    private Class<?>[] allClasses = new Class[0];
    private Map<String, Class<?>> controllerClasses = new HashMap<>();
    private Map<String, Class<?>> componentClasses = new HashMap<>();
    private Map<String, Class<?>> beanClasses = new HashMap<>();

    private static BeanLoader instance;

    private BeanLoader() {
        try {
            allClasses = ClassLoader.getClasses("com.board");
            this.controllerClasses = this.initComponentClasses(controllerType);
            this.componentClasses = this.initComponentClasses(componentTypes);
            this.beanClasses = this.initBeanMethods();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static BeanLoader getInstance() {
        if (instance == null) instance = new BeanLoader();
        return instance;
    }

    private Map<String, Class<?>> initComponentClasses(List<Class<?>> componentTypes) {
        Map<String, Class<?>> result = new HashMap<>();
        for (Class<?> clazz : allClasses) {
            Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();
            for (Annotation annotation : declaredAnnotations) {
                if (componentTypes.contains(annotation.annotationType())) {
                    String beanName = clazz.getSimpleName();
                    beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
                    result.put(beanName, clazz);
                    System.out.println("create " + beanName + " - " + clazz.getName());
                }
            }
        }
        return result;
    }

    private Map<String, Class<?>> initBeanMethods() {
        Map<String, Class<?>> result = new HashMap<>();

        Set<String> keys = componentClasses.keySet();
        for (String key : keys) {
            Method[] declaredMethods = componentClasses.get(key).getDeclaredMethods();
            for (Method method : declaredMethods) {
                Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
                for (Annotation annotation : declaredAnnotations) {
                    if (annotation.annotationType().equals(Bean.class)) {
                        try {
                            Object instance = method.invoke(componentClasses.get(key).newInstance());
                            result.put(method.getName(), instance.getClass());
                            System.out.println("create " + method.getName() + " - " + method.getReturnType());
                        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return result;
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
