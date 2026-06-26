package support;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import db.MapperExecutor;
import exception.ServiceAssert;
import model.User;

import javax.swing.filechooser.FileSystemView;

public class UserSupporter {

    /**
     * 桌面路径
     */
    public final static String DESKTOP_PATH = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

    /**
     * 登录态存放文件
     */
    private static final String CURRENT_USER_PATH = DESKTOP_PATH + "\\.breakout\\current-user.txt";

    /**
     * 用户登录
     *
     * @param userId   用户账号
     * @param password 用户密码
     */
    public static void login(String userId, String password) {

        ServiceAssert.isTrue(!StrUtil.hasBlank(userId, password), "请输入账号&密码");

        User user = MapperExecutor.query(mapper -> mapper.findByUserId(userId));
        ServiceAssert.isTrue(!ObjectUtil.isNull(user), "当前用户[{}]不存在", userId);

        ServiceAssert.isTrue(password.equals(user.getPassword()), "用户密码不正确");

        FileUtil.mkParentDirs(CURRENT_USER_PATH);
        FileUtil.writeUtf8String(userId, CURRENT_USER_PATH);
    }

    public static void register(String userId, String password) {

        ServiceAssert.isTrue(!StrUtil.hasBlank(userId, password), "请输入账号&密码");

        User user = MapperExecutor.query(mapper -> mapper.findByUserId(userId));
        ServiceAssert.isTrue(ObjectUtil.isNull(user), "当前用户[{}]已存在", userId);

        MapperExecutor.execute(mapper -> mapper.createUser(userId, password));
    }

    public static String loadCurrentLoggedInUser() {

        if (!FileUtil.exist(CURRENT_USER_PATH)) {
            return null;
        }

        String userId;
        if (StrUtil.isBlank(userId = FileUtil.readUtf8String(CURRENT_USER_PATH))) {
            return null;
        }

        if (ObjectUtil.isNull(MapperExecutor.query(mapper -> mapper.findByUserId(userId)))) {
            FileUtil.writeUtf8String(CURRENT_USER_PATH, "");
            return "";
        }

        return userId;
    }

    public static void logout() {
        FileUtil.del(CURRENT_USER_PATH);
    }

}
