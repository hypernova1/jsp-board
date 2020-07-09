package sam.board.controller;

import sam.board.domain.Post;
import sam.board.service.PostService;
import sam.spring.annotation.Autowired;
import sam.spring.annotation.RequestBody;
import sam.spring.annotation.component.RestController;
import sam.spring.annotation.mapping.GetMapping;
import sam.spring.annotation.mapping.PostMapping;
import sam.spring.annotation.mapping.PutMapping;
import sam.spring.annotation.mapping.RequestMapping;
import sam.spring.view.Model;

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
