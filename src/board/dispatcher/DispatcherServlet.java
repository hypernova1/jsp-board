package board.dispatcher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

@WebServlet("/board/*")
public class DispatcherServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestHandlerMapping requestHandlerMapping = new RequestHandlerMapping();
        Map<String, Object> target = requestHandlerMapping.execute(request);
        RequestHandlerAdaptor requestHandlerAdaptor = new RequestHandlerAdaptor();
        String viewPath = requestHandlerAdaptor.execute(target, request, response);

        ViewResolver viewResolver = ViewResolver.getInstance();
        viewResolver.setPrefix("/WEB-INF/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setPath(viewPath);
        viewResolver.execute(request, response);
    }

}
