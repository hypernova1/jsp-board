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

    @PutMapping("/update")
    public String updatePost(Post post) {
        post.setSeq(1);
        postService.update(post);
        return "redirect:/post/" + post.getSeq();
    }


}
