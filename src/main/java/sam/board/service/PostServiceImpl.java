package sam.board.service;

import sam.board.dao.PostDao;
import sam.board.domain.Post;
import sam.spring.annotation.component.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostDao postDao;

    public PostServiceImpl() {
        postDao = PostDao.getInstance();
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
