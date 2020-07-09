package sam.board.controller;

import sam.board.domain.Post;
import sam.spring.annotation.component.Controller;
import sam.spring.annotation.mapping.GetMapping;
import sam.spring.view.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @GetMapping("/")
    public String toMain(Post post, Model model, HttpServletRequest request, HttpServletResponse response) {
        model.setAttribute("title", post.getTitle());
        return "main";
    }

}
