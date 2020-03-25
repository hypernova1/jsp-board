package com.board.service;

import com.board.dao.PostDao;
import com.board.domain.Post;
import spring.annotation.component.Service;

import java.util.List;

@Service
public class PostService {

    private final PostDao postDao;
    private static PostService instance;

    public PostService() {
        postDao = PostDao.getInstance();
    }

    public static PostService getInstance() {
        if (instance == null) instance = new PostService();
        return instance;
    }

    public Post selectOne(int seq) {
        return postDao.selectOne(seq);
    }

    public List<Post> selectAll() {
        return postDao.selectAll();

    }

    public void update(Post post) {
        postDao.update(post);
    }

    public int insert(Post post) {
        return postDao.insert(post);
    }
}
