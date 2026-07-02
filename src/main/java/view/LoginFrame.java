package view;

import support.GameSupporter;
import ui.*;

import javax.swing.*;
import java.awt.*;

import static ui.GameFonts.FORM_TEXT;

public class LoginFrame extends AbstractGameFrame {

    private static final long serialVersionUID = 1504087619736129919L;

    /**
     * 用户账号输入框
     */
    private JTextField userIdField;

    /**
     * 用户密码输入框
     */
    private JPasswordField passwordField;

    /**
     * 以弹窗形式打开登录表单
     *
     * @param parent     父组件
     * @param afterLogin 登录成功后的操作
     */
    public static void openDialog(Component parent, SwingOperation afterLogin) {
        new LoginFrame(parent, afterLogin);
    }

    /**
     * 打开登录弹窗
     *
     * @param parent     父组件
     * @param afterLogin 登录成功后的操作
     */
    private LoginFrame(Component parent, SwingOperation afterLogin) {
        this.showDialog(parent, afterLogin);
    }

    /**
     * 构建用户登录表单
     *
     * @param panel 当前窗口的根面板
     */
    @Override
    protected void buildContent(JPanel panel) {
        this.buildLoginContent(panel, this,
                () -> this.login(() -> SwingWindows.hideAndOpen(this, MainMenuFrame::new)),
                () -> SwingWindows.hideAndOpen(this, MainMenuFrame::new));
    }

    /**
     * 构建登录表单内容
     *
     * @param panel        当前窗口的根面板
     * @param parent       父组件
     * @param loginAction  登录动作
     * @param cancelAction 取消动作
     */
    private void buildLoginContent(JPanel panel, Component parent, SwingOperation loginAction, SwingOperation cancelAction) {

        SwingFormFactory formFactory = SwingFormFactory.with(panel, FORM_TEXT);

        JLabel title = formFactory.label("用户登录", 0, 30, WINDOW_WIDTH, 42, TITLE_COLOR);
        title.setFont(TITLE_FONT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        formFactory.label("账号", 105, 102, 90, 30, LABEL_TEXT_COLOR);
        decorateTextField(userIdField = formFactory.textField(200, 102, 190, 30));

        formFactory.label("密码", 105, 148, 90, 30, LABEL_TEXT_COLOR);
        decorateTextField(passwordField = formFactory.passwordField(200, 148, 190, 30));

        JButton backHomeButton = formFactory.button(new ArcadeMenuButton("回到主界面"), 100, 218, 132, 40, BUTTON_GRAY);
        backHomeButton.setForeground(Color.WHITE);
        backHomeButton.setFont(FORM_TEXT);
        backHomeButton.setFocusPainted(false);
        backHomeButton.setBorderPainted(false);

        JButton loginButton = formFactory.button(new ArcadeMenuButton("确认"), 268, 218, 132, 40, BUTTON_BLUE);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(FORM_TEXT);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);

        SwingActionFactory.with(parent).bind(loginButton, loginAction).bind(backHomeButton, cancelAction);
    }

    /**
     * 展示登录弹窗
     *
     * @param parent     父组件
     * @param afterLogin 登录成功后的操作
     */
    private void showDialog(Component parent, SwingOperation afterLogin) {

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), "用户登录", Dialog.ModalityType.APPLICATION_MODAL);

        JPanel panel = new JPanel(null);
        panel.setBackground(PANEL_BACKGROUND);
        this.buildLoginContent(panel, dialog, () -> this.login(() -> {
            dialog.dispose();
            afterLogin.execute();
        }), dialog::dispose);

        dialog.setContentPane(panel);
        dialog.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    /**
     * 执行登录并触发后续操作
     *
     * @param afterLogin 登录成功后的操作
     * @throws Exception 登录成功后的操作执行失败时抛出异常
     */
    private void login(SwingOperation afterLogin) throws Exception {
        GameSupporter.login(this.userIdField.getText(), String.valueOf(this.passwordField.getPassword()));
        afterLogin.execute();
    }

    /**
     * 装饰登录页输入框
     *
     * @param textField 输入框
     */
    private static void decorateTextField(JTextField textField) {
        textField.setForeground(LABEL_TEXT_COLOR);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createLineBorder(FIELD_BORDER_COLOR));
    }

    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 登录窗口宽度
     */
    private final static int WINDOW_WIDTH = 500;

    /**
     * 登录窗口高度
     */
    private final static int WINDOW_HEIGHT = 300;

    /**
     * 登录页背景色
     */
    private final static Color PANEL_BACKGROUND = new Color(248, 250, 252);

    /**
     * 登录页标题字体
     */
    private final static Font TITLE_FONT = new Font("黑体", Font.BOLD, 28);

    /**
     * 登录页标题颜色
     */
    private final static Color TITLE_COLOR = new Color(42, 107, 255);

    /**
     * 登录页标签颜色
     */
    private final static Color LABEL_TEXT_COLOR = new Color(38, 50, 56);

    /**
     * 登录页输入框边框颜色
     */
    private final static Color FIELD_BORDER_COLOR = new Color(207, 216, 220);

    /**
     * 登录页确认按钮色
     */
    private final static Color BUTTON_BLUE = new Color(42, 107, 255);

    /**
     * 登录页返回按钮色
     */
    private final static Color BUTTON_GRAY = new Color(84, 110, 122);

}
