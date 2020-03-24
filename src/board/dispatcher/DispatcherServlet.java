package board.dispatcher;

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
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        board.dispatcher.RequestHandlerMapping requestHandlerMapping = new board.dispatcher.RequestHandlerMapping();
        Map<String, Object> target = requestHandlerMapping.execute(request);

        board.dispatcher.RequestHandlerAdaptor requestHandlerAdaptor = new board.dispatcher.RequestHandlerAdaptor();
        String viewPath = requestHandlerAdaptor.execute(target, request, response);

        board.dispatcher.ViewResolver viewResolver = board.dispatcher.ViewResolver.getInstance();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setPath(viewPath);
        viewResolver.execute(request, response);
    }

}
