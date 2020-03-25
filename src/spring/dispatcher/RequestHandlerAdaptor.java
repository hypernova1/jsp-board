package spring.dispatcher;

import spring.view.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class RequestHandlerAdaptor {

    private static RequestHandlerAdaptor instance;

    private RequestHandlerAdaptor() {}

    public static RequestHandlerAdaptor getInstance() {
        if (instance == null) instance = new RequestHandlerAdaptor();
        return instance;
    }

    public String execute(Map<String, Object> target, HttpServletRequest request, HttpServletResponse response) {

        Model model = null;

        if (target == null || target.get("method") == null) {
            return "";
        }

        Class<?> clazz = (Class<?>) target.get("instance");
        Method method = (Method) target.get("method");

        Converter<?> converter = new Converter<>();
        Class<?>[] parameterClasses = method.getParameterTypes();
        List<Object> parameterList = new ArrayList<>();
        for (Class<?> parameterClass : parameterClasses) {
            if (parameterClass.getSimpleName().equals("Model")) {
                model = new Model();
                parameterList.add(model);
                continue;
            }
            if (parameterClass.getName().startsWith("javax.servlet")) {
                if (parameterClass.getSimpleName().contains("Request")) {
                    parameterList.add(request);
                    continue;
                }
                if (parameterClass.getSimpleName().contains("Response")) {
                    parameterList.add(response);
                    continue;
                }
            }
            Object parameter = converter.execute(request, parameterClass);
            parameterList.add(parameter);
        }

        Object result = null;
        try {
            result = method.invoke(clazz.newInstance(), parameterList.toArray(new Object[0]));
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
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
