package spring.web;

import spring.common.Converter;
import spring.view.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class RequestHandlerAdaptor implements HandlerAdaptor {

    private static RequestHandlerAdaptor instance;

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
            if (parameter.getType().isPrimitive()) {
                System.out.println(parameter.getName());
                parameterList.add(request.getParameter(parameter.getName()));
                continue;
            }
            Object objParameter = converter.execute(request, parameter.getType());
            parameterList.add(objParameter);
        }

        Object result = null;
        try {
            result = method.invoke(instance, parameterList.toArray(new Object[0]));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        if (!Objects.isNull(model)) {
            setParameterForModel(model, request);
        }

        return result.toString();

    }

    private void setParameterForModel(Model model, HttpServletRequest request) {

        Field modelAttributes = null;
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
