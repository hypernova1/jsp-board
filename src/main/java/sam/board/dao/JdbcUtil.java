package sam.board.dao;

import sam.board.domain.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUtil {

    private final String driver = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost:3306/board";
    private final String user = "root";
    private final String password = "1111";

    private Connection conn = null;

    private static JdbcUtil instance = null;

    private JdbcUtil() {}

    public static JdbcUtil getInstance() {
        if (instance == null) instance = new JdbcUtil();
        return instance;
    }

    public Connection getConnection() {
        try {
            Class.forName(driver);
            if (conn == null) {
                this.conn = DriverManager.getConnection(url, user, password);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this.conn;
    }

    public void closeConnection() {
        try {
            this.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insert(String sql, List<String> params) {

        int result = 0;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            result = ps.executeUpdate();

            if (result == 0) throw new SQLException("create fail!!");

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    result = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Post> selectAll(String sql) {

        List<Post> postList = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Post post = new Post();
                post.setSeq(rs.getInt("seq"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));

                postList.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return postList;

    }

    public Post selectOne(String sql, int seq) {

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, seq);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String title = rs.getString("title");
                String content = rs.getString("content");

                Post post = new Post();
                post.setSeq(seq);
                post.setTitle(title);
                post.setContent(content);
                return post;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
