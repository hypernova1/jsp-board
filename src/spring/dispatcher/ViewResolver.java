package spring.dispatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ViewResolver {

    private String prefix;
    private String suffix;
    private String path;

    public ViewResolver(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (path.equals("")) path = "error/404";
            if (path.startsWith("redirect:")) {
                path = path.replace("redirect:", "");
                response.sendRedirect(path);
                return;
            }

            System.out.println(prefix + path + suffix);
            request.getRequestDispatcher(prefix + path + suffix).include(request, response);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }

    public void setPath(String path) {
        this.path = path;
    }
}
