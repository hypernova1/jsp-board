package java.board.controller;

import java.board.domain.Post;
import java.board.service.PostService;
import spring.annotation.Autowired;
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

    @Autowired
    private PostService postService;

    @RequestMapping("/list")
    public String getList(Model model) {
        List<Post> postList = postService.selectAll();
        model.setAttribute("postList", postList);
        return "post/list";
    }

    @GetMapping("/view")
    public String getName(int seq, Model model) {

        System.out.println(seq);
//        Post post = postService.selectOne(seq);
//        model.setAttribute("post", post);
        return "post/view";
    }

    @GetMapping("/register")
    public String goRegister(Post post) {
        return "post/register";
    }

    @PostMapping("/register")
    public String registerPost(Post post) {
        postService.insert(post);
        return "redirect:post/list";
    }

    @PutMapping("/update")
    public String updatePost(Post post) {
        post.setSeq(1);
        postService.update(post);
        return "redirect:/post/" + post.getSeq();
    }


}
