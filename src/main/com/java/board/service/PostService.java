package java.board.service;

import java.board.domain.Post;

import java.util.List;

public interface PostService {

    List<Post> selectAll();
    void update(Post post);
    int insert(Post post);
    Post selectOne(int seq);
}
