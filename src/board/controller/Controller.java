package board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {

    String execute(HttpServletRequest request, HttpServletResponse response);

    String doGet(HttpServletRequest request, HttpServletResponse response);
    String doPost(HttpServletRequest request, HttpServletResponse response);

    default String classifyMethod(HttpServletRequest request, HttpServletResponse response) {
        String path;
        switch (request.getMethod()) {
            case "GET": path =  doGet(request, response);
                break;
            case "POST": path =  doPost(request, response);
                break;
            default: path = "";
        }
        return path;
    }
}
