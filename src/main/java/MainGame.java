import cn.hutool.core.lang.Opt;
import ui.SwingActionFactory;
import ui.SwingFormFactory;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;

public class MainGame extends JFrame {

    private static final long serialVersionUID = -4482258986636771110L;

    private static final int WINDOW_X = 550;

    private static final int WINDOW_Y = 80;

    private static final int WINDOW_WIDTH = 550;

    private static final int WINDOW_HEIGHT = 700;

    private static final int MENU_BUTTON_X = 190;

    private static final int MENU_BUTTON_WIDTH = 180;

    private static final int MENU_BUTTON_HEIGHT = 60;

    public MainGame() {

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Color.WHITE);

        SwingFormFactory formFactory = SwingFormFactory.with(mainPanel, new Font("黑体", Font.BOLD, 26));
        SwingActionFactory actionFactory = SwingActionFactory.with(this);

        Opt<String> currentUserId = GameSupporter.loadCurrentLoggedInUserId();
        if (currentUserId.isPresent()) {
            formFactory.label(this.buildGreetingPrefix() + currentUserId.get(), 0, 470, WINDOW_WIDTH, MENU_BUTTON_HEIGHT).setHorizontalAlignment(JLabel.CENTER);
            actionFactory.bind(this.menuButton(formFactory, "退出登录", 550, Color.RED), this::logout);
        } else {
            actionFactory.bind(this.menuButton(formFactory, "注册", 470, Color.WHITE), Register::new)
                    .bind(this.menuButton(formFactory, "登陆", 550, Color.WHITE), this::openLogin);
        }

        actionFactory
                .bind(this.menuButton(formFactory, "单人游戏", 110, Color.WHITE), this::startSingleGame)
                .bind(this.menuButton(formFactory, "游戏设置", 190, Color.WHITE), this::openGameSetting)
                .bind(this.menuButton(formFactory, "英雄榜", 270, Color.WHITE), this::openRecordList)
                .bind(this.menuButton(formFactory, "退出游戏", 350, Color.WHITE), () -> System.exit(1));

        this.add(mainPanel);
        this.setBackground(Color.WHITE);
        this.setBounds(WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle("打~砖~块");
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
     */
    private void startSingleGame() {

        if (this.hasLoggedIn()) {
            this.setVisible(false);
            this.dispose();
            SwingUtilities.invokeLater(() -> JBreakout.open(this));
        } else {
            this.openLoginIfConfirmed();
        }

    }

    /**
     * 打开游戏设置窗口
     */
    private void openGameSetting() {
        if (this.hasLoggedIn()) {
            new Setting();
        } else {
            this.openLoginIfConfirmed();
        }
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
     */
    private void openRecordList() {

        if (this.hasLoggedIn()) {
            new RecordList();
            this.setVisible(false);
        } else {
            this.openLoginIfConfirmed();
        }
    }

    /**
     * 判断当前是否已有登录用户
     *
     * @return boolean 是否已登录
     */
    private boolean hasLoggedIn() {
        return GameSupporter.loadCurrentLoggedInUserId().isPresent();
    }

    /**
     * 未登录时询问是否跳转登录
     */
    private void openLoginIfConfirmed() {
        int result = JOptionPane.showConfirmDialog(this, "请先登录！", "提示", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            this.setVisible(false);
            new Login();
        }
    }

    public static void main(String[] args) {
        new MainGame();
    }

}
