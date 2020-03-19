package dao;

import dto.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDao {

    private JdbcUtil jdbcUtil = null;

    public PostDao() {
        this.jdbcUtil = JdbcUtil.getInstance();
    }

    public void insertPost(Post post) {

        List<String> params = new ArrayList<>();
        params.add(post.getTitle());
        params.add(post.getContent());

        boolean result = jdbcUtil.insert("INSERT INTO board (title, content) VALUES (?, ?)", params);

        System.out.println(result);

//        try {
//            while (rs.next()) {
//                int seq = rs.getInt("seq");
//                String title = rs.getString("title");
//                String content = rs.getString("content");
//                System.out.println(title + " " + content);
//
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }


    }

}
