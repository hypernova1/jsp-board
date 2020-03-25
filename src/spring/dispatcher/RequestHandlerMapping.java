package spring.dispatcher;

import spring.annotation.mapping.GetMapping;
import spring.annotation.mapping.PostMapping;
import spring.annotation.mapping.PutMapping;
import spring.annotation.mapping.RequestMapping;
import spring.core.BeanLoader;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHandlerMapping {

    private List<Class<?>> mappingNames = Arrays.asList(RequestMapping.class, GetMapping.class, PostMapping.class, PutMapping.class);
    private BeanLoader beanLoader;

    private static RequestHandlerMapping instance;

    private RequestHandlerMapping() {
        beanLoader = BeanLoader.getInstance();
    }

    public static RequestHandlerMapping getInstance() {
        if (instance == null) instance = new RequestHandlerMapping();
        return instance;
    }

    public Map<String, Object> execute(HttpServletRequest request) {

        String requestPath = request.getPathInfo();
        String requestMethod = request.getMethod();

        Map<String, Object> resultMap = new HashMap<>();

        requestPath = requestPath == null ? "/" : requestPath;
        if (!requestPath.equals("/") && requestPath.endsWith("/")) {
            requestPath = requestPath.substring(0, requestPath.length() - 1);
        }

        List<Class<?>> controllerClasses = beanLoader.getControllerClasses();
        for (Class<?> controller : controllerClasses) {
            RequestMapping requestMappingOnClass = (RequestMapping) getRequestMappingOnClass(controller);

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
            Method[] methods = controller.getDeclaredMethods();
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
                    resultMap.put("instance", controller);
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
