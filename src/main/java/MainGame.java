import cn.hutool.core.lang.Opt;
import db.DataSourceBootstrap;
import ui.AbstractGameFrame;
import ui.GameWindowConfig;
import ui.SwingActionExecutor;
import ui.SwingActionFactory;
import ui.SwingFormFactory;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;

import static ui.GameFonts.MAIN_MENU_TEXT;

public class MainGame extends AbstractGameFrame {

    private static final long serialVersionUID = -4482258986636771110L;

    /**
     * 主菜单窗口横坐标
     */
    private final static int WINDOW_X = 550;

    /**
     * 主菜单窗口纵坐标
     */
    private final static int WINDOW_Y = 80;

    /**
     * 主菜单窗口宽度
     */
    private final static int WINDOW_WIDTH = 550;

    /**
     * 主菜单窗口高度
     */
    private final static int WINDOW_HEIGHT = 700;

    /**
     * 主菜单按钮横坐标
     */
    private final static int MENU_BUTTON_X = 190;

    /**
     * 主菜单按钮宽度
     */
    private final static int MENU_BUTTON_WIDTH = 180;

    /**
     * 主菜单按钮高度
     */
    private final static int MENU_BUTTON_HEIGHT = 60;

    public MainGame() {
        this.openWindow(GameWindowConfig.of("打~砖~块", WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT).setCloseOperation(JFrame.EXIT_ON_CLOSE));
    }

    /**
     * 构建主菜单内容，并为需要登录的入口绑定统一登录守卫
     *
     * @param panel 当前窗口的根面板
     */
    @Override
    protected void buildContent(JPanel panel) {
        SwingFormFactory formFactory = SwingFormFactory.with(panel, MAIN_MENU_TEXT);
        SwingActionFactory actionFactory = SwingActionFactory.with(this);

        Opt<String> currentUserId = GameSupporter.loadCurrentLoggedInUserId();
        if (currentUserId.isPresent()) {
            formFactory.label(this.buildGreetingPrefix() + currentUserId.get(), 0, 470, WINDOW_WIDTH, MENU_BUTTON_HEIGHT).setHorizontalAlignment(JLabel.CENTER);
            actionFactory.bind(this.menuButton(formFactory, "退出登录", 550, Color.RED), this::logout);
        } else {
            actionFactory
                    .bind(this.menuButton(formFactory, "注册", 470, Color.WHITE), Register::new)
                    .bind(this.menuButton(formFactory, "登陆", 550, Color.WHITE), this::openLogin);
        }

        actionFactory
                .bind(this.menuButton(formFactory, "单人游戏", 110, Color.WHITE), this::startSingleGame)
                .bind(this.menuButton(formFactory, "游戏设置", 190, Color.WHITE), this::openGameSetting)
                .bind(this.menuButton(formFactory, "英雄榜", 270, Color.WHITE), this::openRecordList)
                .bind(this.menuButton(formFactory, "退出游戏", 350, Color.WHITE), () -> System.exit(1));
    }

    /**
     * 创建主菜单按钮并应用统一外观
     *
     * @param formFactory 表单组件工厂
     * @param buttonName  按钮文案
     * @param y           纵坐标
     * @param background  背景色
     * @return {@link JButton} 主菜单按钮
     */
    private JButton menuButton(SwingFormFactory formFactory, String buttonName, int y, Color background) {
        JButton button = formFactory.button(buttonName, MENU_BUTTON_X, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, background);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }

    /**
     * 根据当前时间生成首页问候语前缀
     *
     * @return {@link String} 问候语前缀
     */
    private String buildGreetingPrefix() {

        int hour = LocalTime.now().getHour();

        if (hour < 6) {
            return "清晨好，";
        }
        if (hour < 12) {
            return "上午好，";
        }
        if (hour < 14) {
            return "中午好，";
        }
        if (hour < 18) {
            return "下午好，";
        }
        return "晚上好，";
    }

    /**
     * 进入单人游戏
     *
     * @throws Exception 打开游戏失败时抛出异常
     */
    private void startSingleGame() throws Exception {
        LoginGuard.of(this).run(() -> {
            this.setVisible(false);
            this.dispose();
            SwingUtilities.invokeLater(() -> JBreakout.open(this));
        });
    }

    /**
     * 打开游戏设置窗口
     *
     * @throws Exception 打开设置失败时抛出异常
     */
    private void openGameSetting() throws Exception {
        LoginGuard.of(this).run(Setting::new);
    }

    /**
     * 打开登录窗口
     */
    private void openLogin() {
        this.setVisible(false);
        new Login();
    }

    /**
     * 清理当前登录人并回到首页
     */
    private void logout() {
        GameSupporter.logout();
        JOptionPane.showMessageDialog(this, "退出登录成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        this.setVisible(false);
        new MainGame();
    }

    /**
     * 打开排行榜窗口
     *
     * @throws Exception 打开排行榜失败时抛出异常
     */
    private void openRecordList() throws Exception {
        LoginGuard.of(this).run(() -> {
            new RecordList();
            this.setVisible(false);
        });
    }

    public static void main(String[] args) {
        SwingActionExecutor.execute(null, () -> {
            DataSourceBootstrap.initialize();
            SwingUtilities.invokeLater(() -> SwingActionExecutor.execute(null, MainGame::new));
        });
    }

}
