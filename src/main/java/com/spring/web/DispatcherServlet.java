package com.spring.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/board/*")
public class DispatcherServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        RequestHandlerMapping requestHandlerMapping = RequestHandlerMapping.getInstance();
        Map<String, Object> target = requestHandlerMapping.getHandler(request);

        String path = this.getPath(request, response, target);

        if (path.startsWith("redirect:")) {
            path = path.replace("redirect:", "");
            response.sendRedirect(path);
            return;
        }
        request.getRequestDispatcher(path).include(request, response);
    }

    private String getPath(HttpServletRequest request, HttpServletResponse response, Map<String, Object> target) {
        RequestHandlerAdaptor requestHandlerAdaptor = RequestHandlerAdaptor.getInstance();
        String viewPath = requestHandlerAdaptor.handle(target, request, response);
        ViewResolver viewResolver = new ViewResolver("/WEB-INF/jsp/", ".jsp");
        viewResolver.setPath(viewPath);
        return viewResolver.getPath();
    }

}
