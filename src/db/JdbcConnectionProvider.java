package db;

import exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class JdbcConnectionProvider {

    private final static String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/breakout?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&&serverTimezone=Asia/Shanghai";

    /**
     * 获取数据库连接
     *
     * @return {@link Connection}数据库连接
     * @throws SQLException           获取数据库连接失败
     * @throws ClassNotFoundException 数据库驱动加载失败
     */
    public static Connection get() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(JDBC_URL, "root", "123456");
    }

    /**
     * 基于数据库连接信息执行SQL操作
     *
     * @param executor SQL执行器
     * @return {@link T}执行结果
     */
    public static <T> T execute(SqlExecutor<T> executor) {

        try (Connection conn = get()) {
            return executor.execute(conn);
        } catch (SQLException | ClassNotFoundException exception) {
            log.error("数据库操作执行失败: {}", exception.getMessage(), exception);
            throw new ServiceException("数据库执行异常");
        }
    }

}
