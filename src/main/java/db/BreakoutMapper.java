package db;

import model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface BreakoutMapper {

    @Select("select * from user where user_id = #{userId}")
    User findByUserId(String userId);

    @Insert("insert into user(user_id, password, create_time) VALUES(#{userId}, #{password}, now())")
    void createUser(@Param("userId") String userId, @Param("password") String password);

}
