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

    public LoginFrame() {
        this.openWindow(GameWindowConfig.of("用户登录", 650, 330, 380, 280).setCloseOperation(JFrame.DO_NOTHING_ON_CLOSE));
    }

    /**
     * 构建用户登录表单
     *
     * @param panel 当前窗口的根面板
     */
    @Override
    @SuppressWarnings("DuplicatedCode")
    protected void buildContent(JPanel panel) {

        SwingFormFactory formFactory = SwingFormFactory.with(panel, FORM_TEXT);

        formFactory.label("账号", 30, 25, 180, 30);
        userIdField = formFactory.textField(110, 25, 180, 30);

        formFactory.label("密码", 30, 60, 180, 30);
        passwordField = formFactory.passwordField(110, 60, 180, 30);

        SwingActionFactory.with(this)
                .bind(formFactory.button("确认", 230, 140, 110, 30, Color.GREEN), this::login)
                .bind(formFactory.button("回到主界面", 50, 140, 150, 30, Color.GREEN), this::backHome);
    }

    /**
     * 执行登录并回到主界面
     */
    private void login() {
        GameSupporter.login(this.userIdField.getText(), String.valueOf(this.passwordField.getPassword()));
        SwingWindows.hideAndOpen(this, MainMenuFrame::new);
    }

    /**
     * 不登录，直接回到主界面
     */
    private void backHome() {
        SwingWindows.hideAndOpen(this, MainMenuFrame::new);
    }

}
