package board.controller;

import board.domain.Post;
import board.service.PostService;
import board.util.PathUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class PostController implements Controller {

    private final PostService postService;

    public PostController() {
        postService = PostService.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return classifyMethod(request, response);
    }

    @Override
    public String doGet(HttpServletRequest request, HttpServletResponse response) {

        String path = request.getPathInfo().replace("/post", "");
        String viewPath = "";

        if (path.equals("/list") || path.equals("")) {
            List<Post> postList = postService.selectAll();
            request.setAttribute("postList", postList);
            viewPath = "post/list";

        } else if (path.startsWith("/detail")) {
            int seq = PathUtils.getSeqFromPath(path);
            Post post = postService.selectOne(seq);
            request.setAttribute("post", post);
            viewPath = "post/detail";
        } else if (path.startsWith("/register")) {
            viewPath = "post/register";
        }

        return viewPath;
    }

    public String doPost(HttpServletRequest request, HttpServletResponse response) {

        String path = request.getPathInfo().replace("/post", "");
        String viewPath = "";

        if (path.equals("/") || path.equals("")) {

            String title = request.getParameter("title");
            String content = request.getParameter("content");
            Post post = new Post();
            post.setTitle(title);
            post.setContent(content);

            int generatedId = postService.insert(post);
            postService.selectOne(generatedId);
            viewPath = "redirect:/post/" + generatedId;

        } else if (path.startsWith("/update")) {

            path = path.replace("/update", "");
            int seq = PathUtils.getSeqFromPath(path);
            String title = request.getParameter("title");
            String content = request.getParameter("content");

            Post post = new Post();
            post.setSeq(seq);
            post.setTitle(title);
            post.setContent(content);

            postService.update(post);
            viewPath = "redirect:/post/" + seq;
        }

        return viewPath;
    }

}
