package com.spring.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

public class PrimitiveWrapper {

    public static Object autoBoxing(Class<?> type, String value) {

        Constructor<?>[] constructors = type.getConstructors();
        for (Constructor<?> constructor : constructors) {
            Parameter[] parameters = constructor.getParameters();

            for (Parameter parameter : parameters) {
                if (parameter.getType().equals(String.class)) {
                    try {
                        return constructor.newInstance(value);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
