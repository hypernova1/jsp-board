package com.board.service;

import com.board.domain.Post;

import java.util.List;

public interface PostService {

    List<Post> selectAll();
    void update(Post post);
    int insert(Post post);
    Post selectOne(int seq);
}
