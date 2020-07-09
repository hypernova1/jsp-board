package sam.spring.web;

import sam.spring.annotation.ResponseBody;
import sam.spring.annotation.component.RestController;
import sam.spring.annotation.mapping.GetMapping;
import sam.spring.annotation.mapping.PostMapping;
import sam.spring.annotation.mapping.PutMapping;
import sam.spring.annotation.mapping.RequestMapping;
import sam.spring.core.BeanContainer;

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
        Set<String> controllerNames = controllers.keySet();

        for (String controllerName : controllerNames) {
            Class<?> controllerClassType = controllers.get(controllerName).getClass();
            RequestMapping requestMappingOnClass = (RequestMapping) this.getRequestMappingOnClass(controllerClassType);

            boolean isReplacedPath = false;
            String requestMappingValueOnClass = "";
            String requestMappingValueOnMethod = "";
            if (requestMappingOnClass != null) {
                Annotation[] declaredAnnotations = controllers.get(controllerName).getClass().getDeclaredAnnotations();
                requestMappingValueOnClass = requestMappingOnClass.value();
                if (requestPath.startsWith(requestMappingValueOnClass)) {
                    requestMappingValueOnClass = requestMappingValueOnClass.startsWith("/") ? requestMappingValueOnClass : "/" + requestMappingValueOnClass;
                    String replaceRequestPath = this.replaceRequestPath(requestPath, requestMappingValueOnClass);
                    if (!replaceRequestPath.equals(requestPath)) {
                        requestPath = replaceRequestPath;
                        isRestApi = this.isRestAnnotation(declaredAnnotations);
                        isReplacedPath = true;
                    }

                }
            }

            Method[] methods = controllers.get(controllerName).getClass().getDeclaredMethods();
            for (Method method : methods) {
                String mappingMethod = null;
                Annotation requestMappingOnMethod = getRequestMappingOnMethod(method);
                if (requestMappingOnMethod == null) continue;

                Method[] annotationMethods = requestMappingOnMethod.annotationType().getDeclaredMethods();
                for (Method annotationMethod : annotationMethods) {
                    try {
                        if (annotationMethod.getName().equals("value")) {
                            requestMappingValueOnMethod = annotationMethod.invoke(requestMappingOnMethod).toString();
                            requestMappingValueOnMethod = requestMappingValueOnMethod.startsWith("/") ? requestMappingValueOnMethod : "/" + requestMappingValueOnMethod ;
                        }
                        if (annotationMethod.getName().equals("method")) {
                            mappingMethod = annotationMethod.invoke(requestMappingOnMethod).toString();
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }

                if (!isReplacedPath) requestMappingValueOnMethod = requestMappingValueOnClass + requestMappingValueOnMethod;

                if (requestPath.equals(requestMappingValueOnMethod) && requestMethod.equals(mappingMethod)) {
                    if (!isRestApi) {
                        Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
                        isRestApi = this.isRestAnnotation(declaredAnnotations);
                    }

                    resultMap.put("method", method);
                    resultMap.put("instance", controllers.get(controllerName));
                    resultMap.put("isRestApi", isRestApi);
                    return resultMap;
                }
            }
        }
        return null;
    }

    private String replaceRequestPath(String requestPath, String mappingPath) {
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
