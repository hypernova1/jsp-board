package com.spring.web;

import com.spring.annotation.ResponseBody;
import com.spring.annotation.component.RestController;
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
    private List<Class<?>> restAnnotations = Arrays.asList(ResponseBody.class, RestController.class);
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
        String requestMethod = request.getMethod();

        boolean isRestApi = false;

        Map<String, Object> resultMap = new HashMap<>();

        String requestPath = this.getRequestPath(request);

        Map<String, Object> controllers = beanContainer.getControllerMap();
        Set<String> keys = controllers.keySet();

        for (String key : keys) {
            RequestMapping requestMappingOnClass = (RequestMapping) this.getRequestMappingOnClass(controllers.get(key).getClass());

            String mappingPath = null;
            String mappingMethod = null;

            if (requestMappingOnClass != null) {

                Annotation[] declaredAnnotations = controllers.get(key).getClass().getDeclaredAnnotations();

                isRestApi = this.isRestAnnotation(declaredAnnotations);

                mappingPath = requestMappingOnClass.value();
                mappingPath = mappingPath.startsWith("/") ? mappingPath : "/" + mappingPath;

                requestPath = this.getControllerPath(requestPath, mappingPath);
            }
            Method[] methods = controllers.get(key).getClass().getDeclaredMethods();
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
                    if (!isRestApi) {
                        Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
                        isRestApi = this.isRestAnnotation(declaredAnnotations);
                    }

                    resultMap.put("method", method);
                    resultMap.put("instance", controllers.get(key));
                    resultMap.put("isRestApi", isRestApi);
                    return resultMap;
                }
            }
        }
        return null;
    }

    private String getControllerPath(String requestPath, String mappingPath) {
        if (requestPath.contains(mappingPath)) {
             requestPath = requestPath.replace(mappingPath, "");
             requestPath = requestPath.equals("") ? requestPath + "/" : requestPath;
        }
        return requestPath;
    }

    private String getRequestPath(HttpServletRequest request) {
        String requestPath = request.getPathInfo();
        requestPath = requestPath == null ? "/" : requestPath;
        if (!requestPath.equals("/") && requestPath.endsWith("/")) {
            requestPath = requestPath.substring(0, requestPath.length() - 1);
        }
        return requestPath;
    }

    private boolean isRestAnnotation(Annotation[] declaredAnnotations) {
        for (Annotation annotation : declaredAnnotations) {
            if (restAnnotations.contains(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

    private Annotation getRequestMappingOnClass(Class<?> controller) {
        Annotation[] annotations = controller.getDeclaredAnnotations();
        return this.getRequestMapping(annotations);
    }

    private Annotation getRequestMappingOnMethod(Method method) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        return this.getRequestMapping(annotations);
    }

    private Annotation getRequestMapping(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (mappingNames.contains(annotationType)) return annotation;
        }
        return null;
    }


}
