package db;

import dto.GameSetting;
import dto.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface BreakoutMapper {

    @Select("select * from user where user_id = #{userId}")
    User findByUserId(String userId);

    @Insert("insert into user(user_id, password, create_time) VALUES(#{userId}, #{password}, now())")
    void createUser(@Param("userId") String userId, @Param("password") String password);

    @Select("select * from breakout.user u" +
            " where high_score is not null and high_score > 0" +
            " order by high_score desc, high_score_achieved_time")
    List<User> listRecordRanks();

    @Update("update user set high_score = #{highScore}, high_score_achieved_time = now() where user_id = #{userId}")
    void updateHighScore(@Param("userId") String userId, @Param("highScore") int highScore);

    @Update("update user set" +
            " ball_life = #{gameSetting.ballLife}, ball_size = #{gameSetting.ballSize}," +
            " fps = #{gameSetting.fps}, clear_brick_count = #{gameSetting.clearBrickCount}" +
            " where user_id = #{userId}")
    void updateUserGameSetting(@Param("userId") String userId, @Param("gameSetting") GameSetting gameSetting);

    @Select("select ifnull(max(high_score), 0) from user")
    int fetchCurrentLoggedInUserHighScore();

}
