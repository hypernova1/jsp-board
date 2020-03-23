package board.dispatcher;

import board.annotation.RequestMapping;
import board.reflect.ClassReflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RequestHandlerMapping {

    List<String> mappingNames = Arrays.asList("RequestMapping", "GetMapping", "PostMapping", "PutMapping", "DeleteMapping");

    public Method execute(String requestPath) {
        requestPath = requestPath == null ? "/" : requestPath; // /post/list

        List<Class<?>> controllerClasses = getControllerClass();
        for (Class<?> controller : controllerClasses) {
            RequestMapping requestMappingOnClass = getRequestMappingOnClass(controller);
            String mappingPath = null;
            if (requestMappingOnClass != null) {
                mappingPath = requestMappingOnClass.path(); // controller = post, method = list
                if (requestPath.contains(mappingPath)) {
                     requestPath = requestPath.replace(mappingPath, "");
                     requestPath = requestPath.replace("/", "");
                }
            }

            Method[] methods = controller.getDeclaredMethods();
            for (Method method : methods) {
                RequestMapping requestMappingOnMethod = getRequestMappingOnMethod(method);
                if (requestMappingOnMethod == null) continue;

                mappingPath = requestMappingOnMethod.path().replace("/", "");
                if (requestPath.equals(mappingPath)) return method;
            }
        }
        return null;
    }

    private RequestMapping getRequestMappingOnClass(Class<?> controller) {
        Annotation[] annotations = controller.getDeclaredAnnotations();
        return getRequestMapping(annotations);
    }

    private RequestMapping getRequestMappingOnMethod(Method method) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        return getRequestMapping(annotations);
    }

    private RequestMapping getRequestMapping(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            String simpleName = annotation.annotationType().getSimpleName();
            if (mappingNames.contains(simpleName)) return (RequestMapping) annotation;
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
