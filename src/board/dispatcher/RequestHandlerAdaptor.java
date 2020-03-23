package board.dispatcher;

import board.domain.Post;
import board.exception.ControllerNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class RequestHandlerAdaptor {

    public String execute(Map<String, Object> target, HttpServletRequest request, HttpServletResponse response) {
        if (target == null || target.get("method") == null) {
            return "";
        }

        Class<?> instance = (Class<?>) target.get("instance");
        Method method = (Method) target.get("method");

        Converter<?> converter = new Converter<>();
        Class<?>[] parameters = method.getParameterTypes();
        for (Class<?> parameter : parameters) {
            Object param = converter.execute(request, parameter);
        }

        Object result = null;
        try {
            result = method.invoke(instance.newInstance());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return result.toString();

    }

}
