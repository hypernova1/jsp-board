package spring.core;

import spring.annotation.component.*;
import spring.reflect.ClassReflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BeanLoader {

    private List<Class<?>> componentTypes = Arrays.asList(Component.class, Service.class, Configuration.class);

    private Class<?>[] allClasses = new Class[0];
    private List<Class<?>> controllerClasses = new ArrayList<>();
    private List<Class<?>> componentClasses = new ArrayList<>();
    private Map<String, Object> beanInstances = new HashMap<>();

    private static BeanLoader instance;

    private BeanLoader() {
        try {
            allClasses = ClassReflect.getClasses("com");
            this.controllerClasses = this.initControllerClasses();
            this.componentClasses = this.initComponentClasses();
            this.beanInstances = this.initBeanMethods();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static BeanLoader getInstance() {
        if (instance == null) instance = new BeanLoader();
        return instance;
    }

    private List<Class<?>> initControllerClasses() {
        List<Class<?>> result = new ArrayList<>();
        for (Class<?> clazz : allClasses) {
            if (Objects.nonNull(clazz.getAnnotation(Controller.class))) {
                result.add(clazz);
            }
        }

        System.out.println("Controller size = " + result.size());
        return result;
    }

    private List<Class<?>> initComponentClasses() {
        List<Class<?>> result = new ArrayList<>();
        for (Class<?> clazz : allClasses) {
            Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();
            for (Annotation annotation : declaredAnnotations) {
                if (componentTypes.contains(annotation.annotationType())) {
                    result.add(clazz);
                }
            }
        }

        System.out.println("Component Class size = " + result.size());
        return result;
    }

    private Map<String, Object> initBeanMethods() {
        Map<String, Object> result = new HashMap<>();

        for (Class<?> clazz : componentClasses) {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
                for (Annotation annotation : declaredAnnotations) {
                    if (annotation.annotationType().equals(Bean.class)) {
                        try {
                            Object instance = method.invoke(clazz.newInstance());
                            result.put(method.getName(), instance);
                        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        System.out.println("Bean size = " + result.size());
        return result;
    }

    public List<Class<?>> getComponentClasses() {
        return this.componentClasses;
    }

    public List<Class<?>> getControllerClasses() {
        return controllerClasses;
    }

    public Map<String, Object> getBeanInstances() {
        return this.beanInstances;
    }

    public Class<?>[] getAllClasses() {
        return this.allClasses;
    }
}
