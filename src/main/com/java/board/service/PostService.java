package board.service;

import board.domain.Post;

import java.util.List;

public interface PostService {

    List<Post> selectAll();
    void update(Post post);
    int insert(Post post);
    Post selectOne(int seq);
}
