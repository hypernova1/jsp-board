package com.spring.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.annotation.RequestBody;
import com.spring.common.Converter;
import com.spring.common.PrimitiveWrapper;
import com.spring.view.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class RequestHandlerAdaptor implements HandlerAdaptor {

    private static RequestHandlerAdaptor instance;
    private final ObjectMapper objectMapper;

    private List<Class<?>> wrapperType = Arrays.asList(Boolean.class, Byte.class,  Short.class, Integer.class, Long.class, Float.class, Double.class, Character.class);
    private RequestHandlerAdaptor() {
        objectMapper = new ObjectMapper();
    }

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
        boolean isRestApi = (boolean) target.get("isRestApi");

        Converter<?> converter = new Converter<>();
        Parameter[] parameters = method.getParameters();
        List<Object> parameterList = new ArrayList<>();

        boolean existRequestBody = false;
        for (Parameter parameter : parameters) {
            if (parameter.getDeclaredAnnotation(RequestBody.class) != null) existRequestBody = true;

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
                Class<?> type = PrimitiveWrapper.getType(parameterType);
                Object autoBoxingValue = PrimitiveWrapper.autoBoxing(type, request.getParameter(parameter.getName()));
                parameterList.add(autoBoxingValue);
                continue;
            }

            Object objParameter = converter.execute(request, parameter.getType(), existRequestBody);
            parameterList.add(objParameter);
        }

        Object result = null;
        try {
            result = method.invoke(instance, parameterList.toArray());

            System.out.println(isRestApi);
            if (isRestApi) flushJson(response, result);

        } catch (IllegalAccessException | InvocationTargetException | IOException e) {
            e.printStackTrace();
        }
        if (!Objects.isNull(model)) {
            setParameterForModel(model, request);
        }

        return result.toString();
    }

    private void flushJson(HttpServletResponse response, Object result) throws IOException {
        String json = objectMapper.writeValueAsString(result);
        response.setContentType("application/json");
        response.setContentLength(json.length());
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
        out.close();
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
