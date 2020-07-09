package sam.board.dao;

import sam.board.domain.Post;

import java.util.ArrayList;
import java.util.List;

public class PostDao {

    private static PostDao postDao = null;

    private JdbcUtil jdbcUtil = null;

    public PostDao() {
        this.jdbcUtil = JdbcUtil.getInstance();
    }

    public static PostDao getInstance() {
        if (postDao == null) postDao = new PostDao();
        return postDao;
    }

    public int insert(Post post) {
        List<String> params = new ArrayList<>();
        params.add(post.getTitle());
        params.add(post.getContent());
        return jdbcUtil.insert("INSERT INTO post (title, content) VALUES (?, ?)", params);
    }

    public List<Post> selectAll() {
        return jdbcUtil.selectAll("SELECT * FROM post");
    }

    public void update(Post post) {

    }

    public Post selectOne(int id) {
        return jdbcUtil.selectOne("SELECT * FROM post WHERE seq = ?", id);
    }
}
