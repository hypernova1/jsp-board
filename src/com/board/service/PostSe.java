package com.board.service;

import com.board.domain.Post;

import java.util.List;

public interface PostSe {

    List<Post> selectAll();
    void update(Post post);

}
