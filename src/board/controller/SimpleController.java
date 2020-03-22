package board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class SimpleController implements Controller {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return classifyMethod(request, response);
    }
}
