package com.board.controller;

import com.board.domain.Post;
import spring.annotation.component.Controller;
import spring.annotation.mapping.GetMapping;
import spring.dispatcher.BeanContainer;
import spring.view.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@
public class HomeController {

    BeanContainer beanContainer = BeanContainer.getInstance();

    @GetMapping("/")
    public String toMain(Post post, Model model, HttpServletRequest request, HttpServletResponse response) {
        System.out.println(beanContainer.getBean("hello"));
        model.setAttribute("title", post.getTitle());
        return "/main";
    }

}
