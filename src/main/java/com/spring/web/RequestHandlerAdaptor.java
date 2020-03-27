package com.spring.web;

import com.spring.common.Converter;
import com.spring.common.PrimitiveWrapper;
import com.spring.view.Model;
import com.sun.beans.finder.PrimitiveWrapperMap;

import javax.lang.model.type.PrimitiveType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.*;
import java.util.*;

public class RequestHandlerAdaptor implements HandlerAdaptor {

    private static RequestHandlerAdaptor instance;

    private List<Class<?>> wrapperType = Arrays.asList(Boolean.class, Byte.class,  Short.class, Integer.class, Long.class, Float.class, Double.class, Character.class);
    private RequestHandlerAdaptor() {}

    public static RequestHandlerAdaptor getInstance() {
        if (instance == null) instance = new RequestHandlerAdaptor();
        return instance;
    }

    public String handle(Map<String, Object> target, HttpServletRequest request, HttpServletResponse response) {

        Model model = null;

        if (target == null || target.get("method") == null) {
            return "";
        }

        Object instance = target.get("instance");
        Method method = (Method) target.get("method");

        Converter<?> converter = new Converter<>();
        Parameter[] parameters = method.getParameters();
        List<Object> parameterList = new ArrayList<>();
        for (Parameter parameter : parameters) {
            if (parameter.getType().equals(Model.class)) {
                model = new Model();
                parameterList.add(model);
                continue;
            }
            if (parameter.getType().getName().startsWith("javax.servlet")) {
                if (parameter.getType().getSimpleName().contains("Request")) {
                    parameterList.add(request);
                    continue;
                }
                if (parameter.getType().getSimpleName().contains("Response")) {
                    parameterList.add(response);
                    continue;
                }
            }
            if (wrapperType.contains(parameter.getType())) {
                Object autoBoxingValue = PrimitiveWrapper.autoBoxing(parameter.getType(), request.getParameter(parameter.getName()));
                parameterList.add(autoBoxingValue);
                continue;
            }
            if (parameter.getType().isPrimitive()) {
                String parameterType = parameter.getType().getSimpleName();
                Class<?> type = PrimitiveWrapperMap.getType(parameterType);
                Object autoBoxingValue = PrimitiveWrapper.autoBoxing(type, request.getParameter(parameter.getName()));
                parameterList.add(autoBoxingValue);
                continue;
            }
            Object objParameter = converter.execute(request, parameter.getType());
            parameterList.add(objParameter);
        }

        Object result = null;
        try {
            result = method.invoke(instance, parameterList.toArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        if (!Objects.isNull(model)) {
            setParameterForModel(model, request);
        }

        return result.toString();
    }

    private void setParameterForModel(Model model, HttpServletRequest request) {

        Field modelAttributes;
        try {
            modelAttributes = model.getClass().getDeclaredField("attributes");
            modelAttributes.setAccessible(true);
            Map<String, Object> attributes = (Map<String, Object>) modelAttributes.get(model);

            Set<String> attributeKeys = attributes.keySet();
            for (String key : attributeKeys) {
                Object attribute = attributes.get(key);
                request.setAttribute(key, attribute);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}
