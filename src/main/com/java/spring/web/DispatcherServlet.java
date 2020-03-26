package spring.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/board/*")
public class DispatcherServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {

        RequestHandlerMapping requestHandlerMapping = RequestHandlerMapping.getInstance();
        Map<String, Object> target = requestHandlerMapping.getHandler(request);

        RequestHandlerAdaptor requestHandlerAdaptor = RequestHandlerAdaptor.getInstance();
        String viewPath = requestHandlerAdaptor.handle(target, request, response);
        ViewResolver viewResolver = new ViewResolver("/WEB-INF/jsp/", ".jsp");
        viewResolver.setPath(viewPath);
        viewResolver.execute(request, response);
    }

}
