package board.dispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public class RequestHandlerAdaptor {

    public String execute(Map<String, Object> target, HttpServletRequest request, HttpServletResponse response) {

        Class<?> instance = (Class<?>) target.get("instance");
        Method method = (Method) target.get("method");

        Parameter[] parameters = method.getParameters();

        Object result = null;
        try {
            result = method.invoke(instance.newInstance());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return result.toString();

    }

}
