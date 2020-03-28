package com.spring.common;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;

public class Converter<T> {

    public T execute(HttpServletRequest request, Class<?> clazz) {
        T instance = null;
        try {
            instance = (T) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {

                String parameterName = parameterNames.nextElement();
                String parameter = request.getParameter(parameterName);

                parameterName = parameterName.substring(0, 1).toUpperCase() + parameterName.substring(1);
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.getName().equals("set" + parameterName)) {
                        Class<?> setterParam = method.getParameterTypes()[0];

                        Object castValue;
                        if (setterParam.isPrimitive()) {
                            String parameterType = setterParam.getSimpleName();
                            Class<?> type = PrimitiveWrapper.getType(parameterType);
                            castValue = PrimitiveWrapper.autoBoxing(type, parameter);
                        } else {
                            castValue = setterParam.cast(parameter);
                        }

                        method.invoke(instance, castValue);
                    }
                }
            }
        } catch(IllegalAccessException | InvocationTargetException e){
                e.printStackTrace();
        }
        return instance;
    }




}
