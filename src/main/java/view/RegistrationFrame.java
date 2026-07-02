package view;

import cn.hutool.core.util.StrUtil;
import exception.ServiceAssert;
import support.GameSupporter;
import ui.*;

import javax.swing.*;
import java.awt.*;

import static ui.GameFonts.FORM_TEXT;

public class RegistrationFrame extends AbstractGameFrame {

    private static final long serialVersionUID = 6048411083655101761L;

    /**
     * 用户账号输入框
     */
    private JTextField userIdField;

    /**
     * 用户密码输入框
     */
    private JPasswordField passwordField;

    /**
     * 确认密码输入框
     */
    private JPasswordField confirmPasswordField;

    /**
     * 打开用户注册窗口
     */
    public RegistrationFrame() {
        this.openWindow(GameWindowConfig.of("用户注册", WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT).setBackground(PANEL_BACKGROUND));
    }

    /**
     * 构建用户注册表单
     *
     * @param panel 当前窗口的根面板
     */
    @Override
    @SuppressWarnings("DuplicatedCode")
    protected void buildContent(JPanel panel) {

        SwingFormFactory formFactory = SwingFormFactory.with(panel, FORM_TEXT);

        JLabel title = formFactory.label("用户注册", 0, 30, WINDOW_WIDTH, 42, TITLE_COLOR);
        title.setFont(TITLE_FONT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        formFactory.label("账号", 105, 104, 90, 30, LABEL_TEXT_COLOR);
        decorateTextField(userIdField = formFactory.textField(200, 104, 190, 30));

        formFactory.label("密码", 105, 148, 90, 30, LABEL_TEXT_COLOR);
        decorateTextField(passwordField = formFactory.passwordField(200, 148, 190, 30));

        formFactory.label("确认密码", 105, 192, 90, 30, LABEL_TEXT_COLOR);
        decorateTextField(confirmPasswordField = formFactory.passwordField(200, 192, 190, 30));

        JButton clearButton = formFactory.button(new ArcadeMenuButton("清空"), 120, 262, 116, 40, BUTTON_GRAY);
        clearButton.setForeground(Color.WHITE);
        clearButton.setFont(FORM_TEXT);
        clearButton.setFocusPainted(false);
        clearButton.setBorderPainted(false);

        JButton registerButton = formFactory.button(new ArcadeMenuButton("确认"), 264, 262, 116, 40, BUTTON_BLUE);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(FORM_TEXT);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);

        SwingActionFactory.with(this).bind(registerButton, this::register).bind(clearButton, this::clearForm);
    }

    /**
     * 装饰注册页输入框
     *
     * @param textField 输入框
     */
    private static void decorateTextField(JTextField textField) {
        textField.setForeground(LABEL_TEXT_COLOR);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createLineBorder(FIELD_BORDER_COLOR));
    }

    /**
     * 校验并创建新用户
     */
    private void register() {

        String password = String.valueOf(this.passwordField.getPassword());
        String confirmPassword = String.valueOf(this.confirmPasswordField.getPassword());

        ServiceAssert.isTrue(!StrUtil.hasBlank(password, confirmPassword), "请正确输入用户密码");
        ServiceAssert.isTrue(password.equals(confirmPassword), "密码不匹配！");

        GameSupporter.register(this.userIdField.getText(), password);
        SwingDialogs.information(this, "注册成功！");
        SwingWindows.hide(this);
    }

    /**
     * 清空注册表单输入
     */
    private void clearForm() {
        this.userIdField.setText(null);
        this.passwordField.setText(null);
        this.confirmPasswordField.setText(null);
    }

    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 注册窗口宽度
     */
    private final static int WINDOW_WIDTH = 500;

    /**
     * 注册窗口高度
     */
    private final static int WINDOW_HEIGHT = 360;

    /**
     * 注册窗口横坐标
     */
    private final static int WINDOW_X = 650;

    /**
     * 注册窗口纵坐标
     */
    private final static int WINDOW_Y = 285;

    /**
     * 注册页标题字体
     */
    private final static Font TITLE_FONT = new Font("黑体", Font.BOLD, 28);

    /**
     * 注册页背景色
     */
    private final static Color PANEL_BACKGROUND = new Color(248, 250, 252);

    /**
     * 注册页标题颜色
     */
    private final static Color TITLE_COLOR = new Color(42, 107, 255);

    /**
     * 注册页标签颜色
     */
    private final static Color LABEL_TEXT_COLOR = new Color(38, 50, 56);

    /**
     * 注册页输入框边框颜色
     */
    private final static Color FIELD_BORDER_COLOR = new Color(207, 216, 220);

    /**
     * 注册页确认按钮色
     */
    private final static Color BUTTON_BLUE = new Color(42, 107, 255);

    /**
     * 注册页清空按钮色
     */
    private final static Color BUTTON_GRAY = new Color(84, 110, 122);

}
