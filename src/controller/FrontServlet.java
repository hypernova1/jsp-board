package controller;

import dao.PostDao;
import dto.Post;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/*")
public class FrontServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PostDao postDao = new PostDao();
        Post post = new Post();
        post.setTitle("title");
        post.setContent("content");
        postDao.insertPost(post);



    }
}
