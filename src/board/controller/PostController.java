package board.controller;

import board.annotation.Controller;
import board.annotation.mapping.GetMapping;
import board.annotation.mapping.PutMapping;
import board.annotation.mapping.RequestMapping;
import board.domain.Post;
import board.service.PostService;
import board.view.Model;

import java.util.List;

@Controller
@RequestMapping(path = "/post")
public class PostController {

    private final PostService postService;

    public PostController() {
        postService = PostService.getInstance();
    }

    @RequestMapping(path = "/list")
    public String getList(Model model) {
        List<Post> postList = postService.selectAll();
        model.setAttribute("postList", postList);
        return "post/list";
    }

    @GetMapping(path = "/view")
    public String getName() {
        System.out.println(2222);
        return "post/view";
    }

    @PutMapping(path = "/update")
    public String updatePost(Post post) {
        post.setSeq(1);
        postService.update(post);
        return "redirect:/post/" + post.getSeq();
    }


}
