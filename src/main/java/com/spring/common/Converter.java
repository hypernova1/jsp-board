package com.spring.common;

import com.fasterxml.jackson.core.JsonProcessingException;
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
            return (T) getObjectToJson(request, clazz);
        }

        return getObjectToParameter(request, clazz, instance);
    }

    private T getObjectToParameter(HttpServletRequest request, Class<?> clazz, T instance) {
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

    private Object getObjectToJson(HttpServletRequest request, Class<?> clazz) {

        try {
            StringBuffer sb = new StringBuffer();
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
