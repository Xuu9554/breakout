package view;

import cn.hutool.core.lang.Opt;
import support.GameSupporter;
import ui.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;

import static ui.GameFonts.MAIN_MENU_TEXT;

public class MainMenuFrame extends AbstractGameFrame {

    private static final long serialVersionUID = -4482258986636771110L;

    /**
     * 主菜单根面板
     */
    private JPanel menuPanel;

    public MainMenuFrame() {

        GameWindowConfig config = GameWindowConfig
                .of("打~砖~块", WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT).setCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.openWindow(config);
    }

    /**
     * 创建主菜单内容面板
     *
     * @param config 窗口配置
     * @return {@link JPanel} 主菜单内容面板
     */
    @Override
    protected JPanel createContentPanel(GameWindowConfig config) {
        return this.menuPanel = new MainMenuPanel(config.getLayout());
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

        JLabel title = formFactory.label("BREAKOUT", 0, 72, WINDOW_WIDTH, 58, Color.WHITE);
        title.setFont(TITLE_FONT);
        title.setHorizontalAlignment(JLabel.CENTER);

        Opt<String> currentUserId = GameSupporter.loadCurrentLoggedInUserId();
        if (currentUserId.isPresent()) {
            JLabel greeting = formFactory.label(this.buildGreetingPrefix() + currentUserId.get(), 0, 515, WINDOW_WIDTH, 40, Color.WHITE);
            greeting.setFont(GREETING_FONT);
            greeting.setHorizontalAlignment(JLabel.CENTER);
            actionFactory.bind(this.menuButton(formFactory, "退出登录", 570, MENU_RED), this::logout);
        } else {
            actionFactory
                    .bind(this.accountButton(formFactory, "注册", 105, MENU_CYAN), RegistrationFrame::new)
                    .bind(this.accountButton(formFactory, "登录", 285, MENU_BLUE), () -> LoginFrame.openDialog(this, this::refreshMenu));
        }

        actionFactory
                .bind(this.menuButton(formFactory, "单人游戏", 210, MENU_BLUE), this::startSingleGame)
                .bind(this.menuButton(formFactory, "游戏设置", 280, MENU_PURPLE), () -> this.runAfterLogin(GameSettingsFrame::new))
                .bind(this.menuButton(formFactory, "英雄榜", 350, MENU_CYAN),
                        () -> this.runAfterLogin(() -> LeaderboardFrame.open(this)))
                .bind(this.menuButton(formFactory, "退出游戏", 420, MENU_DARK), () -> System.exit(1));
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
        JButton button = formFactory.button(new ArcadeMenuButton(buttonName), MENU_BUTTON_X, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, background);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }

    /**
     * 创建账户操作按钮
     *
     * @param formFactory 表单组件工厂
     * @param buttonName  按钮文案
     * @param x           横坐标
     * @param background  背景色
     * @return {@link JButton} 账户操作按钮
     */
    private JButton accountButton(SwingFormFactory formFactory, String buttonName, int x, Color background) {
        JButton button = formFactory.button(new ArcadeMenuButton(buttonName), x, 565, ACCOUNT_BUTTON_WIDTH, ACCOUNT_BUTTON_HEIGHT, background);
        button.setForeground(Color.WHITE);
        button.setFont(GREETING_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }

    /**
     * 刷新主菜单内容
     */
    private void refreshMenu() {
        this.menuPanel.removeAll();
        this.buildContent(this.menuPanel);
        this.menuPanel.revalidate();
        this.menuPanel.repaint();
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
        this.runAfterLogin(() -> {
            SwingWindows.dispose(this);
            SwingUtilities.invokeLater(() -> BreakoutGameFrame.open(this));
        });
    }

    /**
     * 登录后执行操作，未登录时先打开登录弹窗
     *
     * @param operation 登录后执行的操作
     * @throws Exception 操作执行失败时抛出异常
     */
    private void runAfterLogin(SwingOperation operation) throws Exception {

        if (GameSupporter.loadCurrentLoggedInUserId().isPresent()) {
            operation.execute();
        } else if (SwingDialogs.confirm(this, "请先登录！")) {
            LoginFrame.openDialog(this, operation);
        }
    }

    /**
     * 清理当前登录人并回到首页
     */
    private void logout() {
        GameSupporter.logout();
        SwingDialogs.information(this, "退出登录成功！");
        this.refreshMenu();
    }

    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 主菜单窗口横坐标
     */
    private final static int WINDOW_X = 625;

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
    private final static int MENU_BUTTON_X = 155;

    /**
     * 主菜单按钮宽度
     */
    private final static int MENU_BUTTON_WIDTH = 240;

    /**
     * 主菜单按钮高度
     */
    private final static int MENU_BUTTON_HEIGHT = 52;

    /**
     * 主菜单账户按钮宽度
     */
    private final static int ACCOUNT_BUTTON_WIDTH = 160;

    /**
     * 主菜单账户按钮高度
     */
    private final static int ACCOUNT_BUTTON_HEIGHT = 46;

    /**
     * 主标题字体
     */
    private final static Font TITLE_FONT = new Font("Arial", Font.BOLD, 46);

    /**
     * 用户问候字体
     */
    private final static Font GREETING_FONT = new Font("黑体", Font.BOLD, 20);

    /**
     * 主菜单蓝色
     */
    private final static Color MENU_BLUE = new Color(42, 107, 255);

    /**
     * 主菜单紫色
     */
    private final static Color MENU_PURPLE = new Color(123, 31, 162);

    /**
     * 主菜单青色
     */
    private final static Color MENU_CYAN = new Color(0, 131, 143);

    /**
     * 主菜单暗色
     */
    private final static Color MENU_DARK = new Color(38, 50, 56);

    /**
     * 主菜单红色
     */
    private final static Color MENU_RED = new Color(211, 47, 47);

}
