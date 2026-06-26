package db;

import org.apache.ibatis.session.SqlSession;

import java.util.function.Consumer;
import java.util.function.Function;

public class MapperExecutor {

    /**
     * 执行查询类Mapper方法
     *
     * @param function Mapper执行逻辑
     * @return {@link R} 查询结果
     */
    public static <R> R query(Function<BreakoutMapper, R> function) {
        try (SqlSession sqlSession = MyBatisSessionFactory.openSession()) {
            return function.apply(sqlSession.getMapper(BreakoutMapper.class));
        }
    }

    /**
     * 执行无返回值的写入类Mapper方法，并在执行成功后提交事务
     *
     * @param consumer Mapper执行逻辑
     */
    public static void execute(Consumer<BreakoutMapper> consumer) {

        try (SqlSession sqlSession = MyBatisSessionFactory.openSession(false)) {
            consumer.accept(sqlSession.getMapper(BreakoutMapper.class));
            sqlSession.commit();
        }
    }

}
