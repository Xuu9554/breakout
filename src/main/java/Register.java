import cn.hutool.core.util.StrUtil;
import exception.ServiceAssert;
import ui.SwingActionFactory;
import ui.SwingFormFactory;

import javax.swing.*;
import java.awt.*;

public class Register extends JFrame {

    private static final long serialVersionUID = 6048411083655101761L;

    private final JTextField userIdField;

    private final JPasswordField passwordField;

    private final JPasswordField confirmPasswordField;

    public Register() {

        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(null);
        registerPanel.setBackground(Color.white);

        Font font = new Font("黑体", Font.BOLD, 15);
        SwingFormFactory formFactory = SwingFormFactory.with(registerPanel, font);

        formFactory.label("账号", 30, 25, 180, 30);
        userIdField = formFactory.textField(110, 25, 180, 30);

        formFactory.label("密码", 30, 60, 180, 30);
        passwordField = formFactory.passwordField(110, 60, 180, 30);

        formFactory.label("确认密码", 30, 95, 180, 30);
        confirmPasswordField = formFactory.passwordField(110, 95, 180, 30);

        SwingActionFactory.with(this)
                .bind(formFactory.button("确认", 230, 140, 110, 30, Color.GREEN), this::register)
                .bind(formFactory.button("清空", 50, 140, 110, 30, Color.GREEN), this::clearForm);

        this.add(registerPanel);
        this.setTitle("用户注册");
        this.setResizable(false);
        this.setBounds(650, 330, 380, 280);
        this.setVisible(true);
    }

    private void register() {

        String password = String.valueOf(this.passwordField.getPassword());
        String confirmPassword = String.valueOf(this.confirmPasswordField.getPassword());

        ServiceAssert.isTrue(!StrUtil.hasBlank(password, confirmPassword), "请正确输入用户密码");
        ServiceAssert.isTrue(password.equals(confirmPassword), "密码不匹配！");

        GameSupporter.register(this.userIdField.getText(), password);
        JOptionPane.showMessageDialog(Register.this, "注册成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        this.setVisible(false);
    }

    private void clearForm() {
        this.userIdField.setText(null);
        this.passwordField.setText(null);
        this.confirmPasswordField.setText(null);
    }

}
