import cn.hutool.core.util.StrUtil;
import exception.ServiceAssert;
import ui.AbstractGameFrame;
import ui.GameWindowConfig;
import ui.SwingActionFactory;
import ui.SwingFormFactory;

import javax.swing.*;
import java.awt.*;

import static ui.GameFonts.FORM_TEXT;

public class Register extends AbstractGameFrame {

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
    public Register() {
        this.openWindow(GameWindowConfig.of("用户注册", 650, 330, 380, 280));
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

        formFactory.label("账号", 30, 25, 180, 30);
        userIdField = formFactory.textField(110, 25, 180, 30);

        formFactory.label("密码", 30, 60, 180, 30);
        passwordField = formFactory.passwordField(110, 60, 180, 30);

        formFactory.label("确认密码", 30, 95, 180, 30);
        confirmPasswordField = formFactory.passwordField(110, 95, 180, 30);

        SwingActionFactory.with(this)
                .bind(formFactory.button("确认", 230, 140, 110, 30, Color.GREEN), this::register)
                .bind(formFactory.button("清空", 50, 140, 110, 30, Color.GREEN), this::clearForm);
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
        JOptionPane.showMessageDialog(Register.this, "注册成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        this.setVisible(false);
    }

    /**
     * 清空注册表单输入
     */
    private void clearForm() {
        this.userIdField.setText(null);
        this.passwordField.setText(null);
        this.confirmPasswordField.setText(null);
    }

}
