package com.board.controller;

import com.board.domain.Post;
import com.spring.annotation.component.RestController;
import com.spring.annotation.mapping.GetMapping;
import com.spring.view.Model;

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
