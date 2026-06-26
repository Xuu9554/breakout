package db;

import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

public final class MyBatisSessionFactory {

    private final static SqlSessionFactory SQL_SESSION_FACTORY = initializeSqlSessionFactory();

    private MyBatisSessionFactory() {

    }

    public static SqlSession openSession() {
        return SQL_SESSION_FACTORY.openSession();
    }

    public static SqlSession openSession(boolean autoCommit) {
        return SQL_SESSION_FACTORY.openSession(autoCommit);
    }

    private static SqlSessionFactory initializeSqlSessionFactory() {

        TransactionFactory transactionFactory = new JdbcTransactionFactory();

        Environment environment = new Environment("test", transactionFactory, DataSourceProvider.fetchDataSource());

        Configuration configuration = new Configuration(environment);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogImpl(Slf4jImpl.class);

        configuration.addMapper(BreakoutMapper.class);
        return new SqlSessionFactoryBuilder().build(configuration);
    }

}
