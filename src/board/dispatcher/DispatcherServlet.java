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

        String viewPath = "";

        RequestHandlerMapping requestHandlerMapping = new RequestHandlerMapping();
        Map<String, Object> target = requestHandlerMapping.execute(request);
        RequestHandlerAdaptor requestHandlerAdaptor = new RequestHandlerAdaptor();
        requestHandlerAdaptor.execute(target, request, response);

//        if (requestPath.equals("/")) viewPath = "main";
//        else {
//            Controller controller = new PathReflect<Controller>().getController(requestPath);
//            if (controller != null) {
//                viewPath = controller.execute(request, response);
//            }
//        }




        if (viewPath.equals("")) {
            request.getRequestDispatcher("/WEB-INF/error/404.jsp").forward(request, response);
            return;
        }
        if (viewPath.startsWith("redirect:/")) {
            response.sendRedirect(viewPath);
            return;
        }

        request.getRequestDispatcher("/WEB-INF/"+ viewPath + ".jsp").include(request, response);
    }


}
