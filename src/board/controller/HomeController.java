package board.controller;

import board.annotation.Controller;
import board.annotation.mapping.GetMapping;
import board.domain.Post;
import board.view.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @GetMapping("/")
    public String toMain(Post post, Model model, HttpServletRequest request, HttpServletResponse response) {

        model.setAttribute("title", post.getTitle());

        return "/main";
    }

}
