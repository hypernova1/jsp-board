package com.board.controller;

import com.board.domain.Post;
import com.board.service.PostService;
import com.spring.annotation.Autowired;
import com.spring.annotation.RequestBody;
import com.spring.annotation.ResponseBody;
import com.spring.annotation.component.Controller;
import com.spring.annotation.component.RestController;
import com.spring.annotation.mapping.GetMapping;
import com.spring.annotation.mapping.PostMapping;
import com.spring.annotation.mapping.PutMapping;
import com.spring.annotation.mapping.RequestMapping;
import com.spring.view.Model;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @RequestMapping("/list")
    public String getList(Model model) {
        List<Post> postList = postService.selectAll();
        model.setAttribute("postList", postList);
        return "post/list";
    }

    @GetMapping("/view")
        public String getName(Post post, Model model) {
        System.out.println(post.getSeq());
        System.out.println(post.getTitle());
//        Post post = postService.selectOne(seq);
//        model.setAttribute("post", post);
        return "post/view";
    }

    @GetMapping("/register")
    public String goRegister(Post post) {
        return "post/register";
    }

    @PostMapping("/register")
    public Post registerPost(@RequestBody Post post) {
        System.out.println(post.getTitle());
        System.out.println(post.getContent());
        //postService.insert(post);
        return post;
    }

    @PutMapping("/update")
    public String updatePost(Post post) {
        post.setSeq(1);
        postService.update(post);
        return "redirect:/post/" + post.getSeq();
    }


}
