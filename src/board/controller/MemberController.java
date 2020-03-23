package board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@board.annotation.Controller
public class MemberController implements Controller {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return classifyMethod(request, response);
    }

    @Override
    public String doGet(HttpServletRequest request, HttpServletResponse response) {
        return "";
    }

    @Override
    public String doPost(HttpServletRequest request, HttpServletResponse response) {
        return "";
    }

}
