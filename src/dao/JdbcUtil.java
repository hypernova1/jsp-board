package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class JdbcUtil {

    private final String driver = "oracle.jdbc.OracleDriver";
    private final String url = "jdbc:oracle:thin:@localhost:1521:xe";
    private final String user = "system";
    private final String password = "oracle";

    private Connection conn = null;
    private PreparedStatement ps = null;

    private static JdbcUtil instance = null;

    private JdbcUtil() {

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,user, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static JdbcUtil getInstance() {
        if (instance == null) instance = new JdbcUtil();
        return instance;
    }

    public boolean insert(String sql, List<String> params) {
        PreparedStatement ps = null;
        boolean rs = false;
        try {
            ps = conn.prepareStatement(sql);

            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            rs = ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

}
