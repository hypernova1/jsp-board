package sam.jpa.repository;

import sam.board.dao.JdbcUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class SimpleRepository<T, ID extends Number> implements JpaRepository<T, ID> {

    private Class<T> tClass;

    protected List<T> item = new ArrayList<>();
    private final JdbcUtil jdbcUtil;
    private Connection conn = null;

    private String tableName;

    @SuppressWarnings("unchecked")
    private SimpleRepository() {
        this.tClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];

        this.tableName = tClass.getSimpleName();
        this.jdbcUtil = JdbcUtil.getInstance();

    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        return null;
    }

    @Override
    public T save(T t) {

        StringBuilder sqlDeclare = new StringBuilder(String.format("INSERT INTO %s", tableName + " ("));
        StringBuilder sqlValue = new StringBuilder(" VALUES(");

        List<Object> sqlValues = new ArrayList<>();

        Method[] methods = tClass.getMethods();
        Method setId = null;

        for (Method method: methods) {
            String propertyName = method.getName();
            if (propertyName.startsWith("get") || propertyName.startsWith("is")) {
                if (propertyName.startsWith("get")) {
                    sqlDeclare.append(propertyName.replace("get", "").toUpperCase());
                }
                if (propertyName.startsWith("is")) {
                    sqlDeclare.append(propertyName.replace("is", "").toUpperCase());
                }
                try {
                    Object propertyValue = method.invoke(t);
                    sqlValues.add(propertyValue);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                sqlValue.append("?, ");
            }
            if (propertyName.equals("setId")) {
                setId = method;
            }

        }
        String sqlValueStr = sqlValue.substring(0, sqlValue.length() - 2);
        sqlValueStr += ")";

        conn = jdbcUtil.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sqlDeclare + sqlValueStr, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 1; i <= sqlValues.size(); i++) {
                ps.setString(i, sqlValues.get(i - 1).toString());
            }

            int result = ps.executeUpdate();

            if (result == 0) throw new SQLException("create fail!!");

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idValue = generatedKeys.getInt(1);
                    setId.invoke(t, idValue);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        jdbcUtil.closeConnection();
        return t;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteById(ID id) {

    }
}
