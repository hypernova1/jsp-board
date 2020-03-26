package com.spring.web;

import com.spring.annotation.mapping.GetMapping;
import com.spring.annotation.mapping.PostMapping;
import com.spring.annotation.mapping.PutMapping;
import com.spring.annotation.mapping.RequestMapping;
import com.spring.core.BeanContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class RequestHandlerMapping implements HandlerMapping {

    private List<Class<?>> mappingNames = Arrays.asList(RequestMapping.class, GetMapping.class, PostMapping.class, PutMapping.class);
    private BeanContainer beanContainer;

    private static RequestHandlerMapping instance;

    private RequestHandlerMapping() {
        beanContainer = BeanContainer.getInstance();
    }

    public static RequestHandlerMapping getInstance() {
        if (instance == null) instance = new RequestHandlerMapping();
        return instance;
    }

    public Map<String, Object> getHandler(HttpServletRequest request) {

        String requestPath = request.getPathInfo();
        String requestMethod = request.getMethod();

        Map<String, Object> resultMap = new HashMap<>();

        requestPath = requestPath == null ? "/" : requestPath;
        if (!requestPath.equals("/") && requestPath.endsWith("/")) {
            requestPath = requestPath.substring(0, requestPath.length() - 1);
        }

        Map<String, Object> allBean = beanContainer.getControllerMap();

        Set<String> keys = allBean.keySet();

        for (String key : keys) {
            RequestMapping requestMappingOnClass = (RequestMapping) getRequestMappingOnClass(allBean.get(key).getClass());

            String mappingPath = null;
            String mappingMethod = null;

            if (requestMappingOnClass != null) {
                mappingPath = requestMappingOnClass.value();
                mappingPath = mappingPath.startsWith("/") ? mappingPath : "/" + mappingPath;

                if (requestPath.contains(mappingPath)) {
                     requestPath = requestPath.replace(mappingPath, "");
                     requestPath = requestPath.equals("") ? requestPath + "/" : requestPath;
                }
            }
            Method[] methods = allBean.get(key).getClass().getDeclaredMethods();
            for (Method method : methods) {
                Annotation requestMappingOnMethod = getRequestMappingOnMethod(method);

                if (requestMappingOnMethod == null) continue;
                Method[] annotationMethods = requestMappingOnMethod.annotationType().getDeclaredMethods();
                for (Method annotationMethod : annotationMethods) {
                    try {
                        if (annotationMethod.getName().equals("value")) {
                            mappingPath = annotationMethod.invoke(requestMappingOnMethod).toString();
                            mappingPath = mappingPath.startsWith("/") ? mappingPath : "/" + mappingPath ;
                        }
                        if (annotationMethod.getName().equals("method")) {
                            mappingMethod = annotationMethod.invoke(requestMappingOnMethod).toString();
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }

                if (requestPath.equals(mappingPath) && requestMethod.equals(mappingMethod)) {
                    resultMap.put("method", method);
                    resultMap.put("instance", allBean.get(key));
                    return resultMap;
                }
            }
        }
        return null;
    }

    private Annotation getRequestMappingOnClass(Class<?> controller) {
        Annotation[] annotations = controller.getDeclaredAnnotations();
        return getRequestMapping(annotations);
    }

    private Annotation getRequestMappingOnMethod(Method method) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        return getRequestMapping(annotations);
    }

    private Annotation getRequestMapping(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (mappingNames.contains(annotationType)) return annotation;
        }
        return null;
    }


}
