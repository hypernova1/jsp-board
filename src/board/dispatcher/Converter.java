package board.dispatcher;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;

public class Converter<T> {

    public T execute(HttpServletRequest request, Class<?> clazz) {
        T t = null;
        try {
            t = (T) clazz.newInstance();

            Enumeration<String> parameterNames = request.getParameterNames();

            while (parameterNames.hasMoreElements()) {

                String parameterName = parameterNames.nextElement();
                parameterName = parameterName.substring(0, 1).toUpperCase() + parameterName.substring(1);
                String parameter = request.getParameter(parameterName);

                Method[] methods = t.getClass().getDeclaredMethods();

                for (Method method : methods) {
                    if (method.getName().equals("set" + parameter)) {
                        method.invoke(clazz, parameter);
                    }
                }
            }
        } catch(InstantiationException | IllegalAccessException | InvocationTargetException e){
                e.printStackTrace();
        }
        return t;
    }



}
