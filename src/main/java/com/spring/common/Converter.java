package com.spring.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;

public class Converter<T> {

    private ObjectMapper mapper;

    public Converter() {
        mapper = new ObjectMapper();
    }

    public T execute(HttpServletRequest request, Class<?> clazz, boolean existRequestBody) {
        T instance = null;
        try {
            instance = (T) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (existRequestBody) {
            return (T) this.getJsonFromObject(request, clazz);
        }

        this.setInstanceProperties(request, clazz, instance);
        return instance;
    }

    private void setInstanceProperties(HttpServletRequest request, Class<?> clazz, T instance) {
        try {
            Enumeration<String> parameterNames = request.getParameterNames();

            while (parameterNames.hasMoreElements()) {
                String parameterName = parameterNames.nextElement();
                String parameter = request.getParameter(parameterName);

                parameterName = parameterName.substring(0, 1).toUpperCase() + parameterName.substring(1);
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.getName().equals("set" + parameterName)) {
                        this.invokeSetter(instance, parameter, method);
                    }
                }
            }
        } catch(IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }
    }

    private void invokeSetter(T instance, String parameter, Method method) throws IllegalAccessException, InvocationTargetException {
        Class<?> setterParam = method.getParameterTypes()[0];
        Object castedValue = this.getWrapperValue(parameter, setterParam);
        method.invoke(instance, castedValue);
    }

    private Object getWrapperValue(String parameter, Class<?> setterParam) {
        Object wrapperValue;
        if (setterParam.isPrimitive()) {
            String parameterType = setterParam.getSimpleName();
            Class<?> type = PrimitiveWrapper.getType(parameterType);
            wrapperValue = PrimitiveWrapper.autoBoxing(type, parameter);
        } else {
            wrapperValue = setterParam.cast(parameter);
        }
        return wrapperValue;
    }

    private Object getJsonFromObject(HttpServletRequest request, Class<?> clazz) {

        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return mapper.readValue(sb.toString(), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
