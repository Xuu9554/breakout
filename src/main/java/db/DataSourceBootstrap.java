package db;

import exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

import static db.DataSourceProvider.*;

@Slf4j
public class DataSourceBootstrap {

    /**
     * 是否已完成数据库初始化
     */
    private static boolean INITIALIZED = false;

    /**
     * 检查用户表是否存在SQL
     */
    private final static String CHECK_USER_TABLE_SQL = "select count(1) from information_schema.tables where table_schema = 'breakout' and table_name = 'user'";

    /**
     * 创建数据库脚本
     */
    private final static String CREATE_DATABASE_SQL = "create database if not exists breakout charset utf8mb3";

    /**
     * 创建用户表脚本
     */
    private final static String CREATE_USER_TABLE_SQL = ""
            + "create table if not exists breakout.user\n"
            + "(\n"
            + "    user_id varchar(255) not null comment '用户id' primary key,\n"
            + "    password varchar(255) null comment '用户密码',\n"
            + "    create_time datetime null,\n"
            + "    high_score int null comment '最佳成绩',\n"
            + "    high_score_achieved_time datetime null comment '最佳成绩完成时间',\n"
            + "    ball_life int default 1 null comment '球的生命',\n"
            + "    ball_size int default 10 null comment '球的大小',\n"
            + "    fps int default 60 null comment '刷新率',\n"
            + "    clear_brick_count int default 10 null comment '通关所需砖块数'\n"
            + ")\n"
            + "charset = utf8mb3;";

    private DataSourceBootstrap() {

    }

    /**
     * 初始化数据库结构
     */
    public static synchronized void initialize() {

        if (INITIALIZED) {
            return;
        }

        try (Connection conn = DriverManager.getConnection(SERVER_JDBC_URL, USERNAME, PASSWORD)) {

            try (PreparedStatement ps = conn.prepareStatement(CHECK_USER_TABLE_SQL); ResultSet rs = ps.executeQuery()) {

                if (rs.next() && rs.getInt(1) > 0) {
                    INITIALIZED = true;
                    return;
                }
            }

            try (Statement statement = conn.createStatement()) {

                statement.execute(CREATE_DATABASE_SQL);
                statement.execute(CREATE_USER_TABLE_SQL);

                INITIALIZED = true;
                log.info("检测到打砖块数据库结构不存在, 已完成初始化");
            }

        } catch (SQLException e) {
            log.error("初始化数据库失败, 原因: {}", e.getMessage(), e);
            throw new ServiceException("数据库初始化失败");
        }
    }

}
