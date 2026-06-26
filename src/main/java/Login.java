import ui.SwingActionFactory;
import ui.SwingFormFactory;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private static final long serialVersionUID = 1504087619736129919L;

    private final JTextField userIdField;

    private final JPasswordField passwordField;

    public Login() {

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(Color.white);

        Font font = new Font("黑体", Font.BOLD, 15);
        SwingFormFactory formFactory = SwingFormFactory.with(loginPanel, font);

        formFactory.label("账号", 30, 25, 180, 30);
        userIdField = formFactory.textField(110, 25, 180, 30);

        formFactory.label("密码", 30, 60, 180, 30);
        passwordField = formFactory.passwordField(110, 60, 180, 30);

        SwingActionFactory.with(this)
                .bind(formFactory.button("确认", 230, 140, 110, 30, Color.GREEN), this::login)
                .bind(formFactory.button("回到主界面", 50, 140, 150, 30, Color.GREEN), this::backHome);

        this.setTitle("用户登录");
        this.add(loginPanel);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setBounds(650, 330, 380, 280);
        this.setVisible(true);
    }

    private void login() {
        GameSupporter.login(this.userIdField.getText(), String.valueOf(this.passwordField.getPassword()));
        this.setVisible(false);
        new MainGame();
    }

    private void backHome() {
        this.setVisible(false);
        new MainGame();
    }

}
