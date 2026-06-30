package db;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;

public class DataSourceProvider {

    private final static DruidDataSource DATA_SOURCE = new DruidDataSource();

    protected final static String SERVER_JDBC_URL = "jdbc:mysql://127.0.0.1:3306/?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai";

    protected final static String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/breakout?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai";

    protected final static String USERNAME = "root";

    protected final static String PASSWORD = "123456";

    static {

        DATA_SOURCE.setUrl(JDBC_URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);
        DATA_SOURCE.setDriverClassName("com.mysql.cj.jdbc.Driver");

        DATA_SOURCE.setInitialSize(2);
        DATA_SOURCE.setMinIdle(2);
        DATA_SOURCE.setMaxActive(10);
        DATA_SOURCE.setMaxWait(3000);

        DATA_SOURCE.setValidationQuery("select 1");
        DATA_SOURCE.setTestWhileIdle(true);
        DATA_SOURCE.setTestOnBorrow(false);
        DATA_SOURCE.setTestOnReturn(false);

        Runtime.getRuntime().addShutdownHook(new Thread(DataSourceProvider::close, "breakout-datasource-shutdown"));
    }

    private DataSourceProvider() {

    }

    public static DataSource get() {
        return DATA_SOURCE;
    }

    /**
     * 关闭数据库连接池
     */
    public static synchronized void close() {
        if (!DATA_SOURCE.isClosed()) {
            DATA_SOURCE.close();
        }
    }

}
