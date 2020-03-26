package java.board.controller;

import java.board.domain.Post;
import spring.annotation.component.Controller;
import spring.annotation.component.RestController;
import spring.annotation.mapping.GetMapping;
import spring.view.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class HomeController {

    @GetMapping("/")
    public String toMain(Post post, Model model, HttpServletRequest request, HttpServletResponse response) {
        model.setAttribute("title", post.getTitle());
        return "main";
    }

}
