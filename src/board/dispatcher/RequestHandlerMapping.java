package board.dispatcher;

import board.annotation.mapping.RequestMapping;
import board.reflect.ClassReflect;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class RequestHandlerMapping {

    List<String> mappingNames = Arrays.asList("RequestMapping", "GetMapping", "PostMapping", "PutMapping", "DeleteMapping");

    public Map<String, Object> execute(HttpServletRequest request) {

        String requestPath = request.getPathInfo();
        String requestMethod = request.getMethod();

        Map<String, Object> resultMap = new HashMap<>();

        requestPath = requestPath == null ? "/" : requestPath;
        if (requestPath.endsWith("/")) {
            requestPath = requestPath.substring(0, requestPath.length() - 1);
        }

        List<Class<?>> controllerClasses = getControllerClass();
        for (Class<?> controller : controllerClasses) {
            RequestMapping requestMappingOnClass = (RequestMapping) getRequestMappingOnClass(controller);

            String mappingPath = null;
            String mappingMethod = null;

            if (requestMappingOnClass != null) {
                mappingPath = requestMappingOnClass.path();
                if (requestPath.contains(mappingPath)) {
                     requestPath = requestPath.replace(mappingPath, "");
                }
            }
            Method[] methods = controller.getDeclaredMethods();
            for (Method method : methods) {
                Annotation requestMappingOnMethod = getRequestMappingOnMethod(method);

                if (requestMappingOnMethod == null) continue;
                Method[] annotationMethods = requestMappingOnMethod.annotationType().getDeclaredMethods();
                for (Method annotationMethod : annotationMethods) {
                    try {
                        if (annotationMethod.getName().equals("path")) {
                            mappingPath = annotationMethod.invoke(requestMappingOnMethod).toString();
                        }
                        if (annotationMethod.getName().equals("method")) {
                            mappingMethod = annotationMethod.invoke(requestMappingOnMethod).toString();
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }

//                System.out.println(controller.getSimpleName());
//                System.out.println(requestPath);
//                System.out.println(mappingPath);
//                System.out.println(requestPath.equals(mappingPath));
//                System.out.println(requestMethod);
//                System.out.println(mappingMethod);
//                System.out.println(requestMethod.equals(mappingMethod));
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
            String simpleName = annotation.annotationType().getSimpleName();
            if (mappingNames.contains(simpleName)) return annotation;
        }
        return null;
    }

    private List<Class<?>> getControllerClass() {
        Class<?>[] allClasses = new Class[0];

        try {
            allClasses = ClassReflect.getClasses("board");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        List<Class<?>> result = new ArrayList<>();
        for (Class<?> clazz : allClasses) {
            if (Objects.nonNull(clazz.getAnnotation(board.annotation.Controller.class))) {
                result.add(clazz);
            }
        }
        return result;
    }
}
