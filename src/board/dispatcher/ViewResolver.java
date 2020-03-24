package board.dispatcher;

import board.exception.ControllerNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ViewResolver {

    private String prefix;
    private String suffix;
    private String path;

    private static ViewResolver instance;

    private ViewResolver() {
    }

    public static ViewResolver getInstance() {
        if (instance == null) instance = new ViewResolver();
        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (path.equals("")) path = "error/404";

            if (path.startsWith("redirect:/")) {
                path = path.replace("redirect:/", "");
                response.sendRedirect(path);
                return;
            }

            request.getRequestDispatcher(prefix + path + suffix).include(request, response);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
