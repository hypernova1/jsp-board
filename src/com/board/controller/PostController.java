package com.board.controller;

import com.board.domain.Post;
import com.board.service.PostService;
import spring.annotation.component.Controller;
import spring.annotation.mapping.GetMapping;
import spring.annotation.mapping.PostMapping;
import spring.annotation.mapping.PutMapping;
import spring.annotation.mapping.RequestMapping;
import spring.view.Model;

import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController() {
        postService = PostService.getInstance();
    }

    @RequestMapping("/list")
    public String getList(Model model) {
        List<Post> postList = postService.selectAll();
        model.setAttribute("postList", postList);
        return "post/list";
    }

    @GetMapping("/view")
    public String getName() {
        return "post/view";
    }

    @GetMapping("/register")
    public String goRegister() {
        return "post/register";
    }

    @PostMapping("/")
    public String registerPost(Post post) {

        return "redirect:post/list";
    }

    @PutMapping("/update")
    public String updatePost(Post post) {
        post.setSeq(1);
        postService.update(post);
        return "redirect:/post/" + post.getSeq();
    }


}
