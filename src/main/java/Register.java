import cn.hutool.core.util.StrUtil;
import exception.ServiceAssert;
import support.UserSupporter;
import ui.SwingActionExecutor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JFrame implements ActionListener {
    private static final long serialVersionUID = 6048411083655101761L;
    DataBase data = new DataBase();
    JButton cleanButton = new JButton("清空");
    JButton confirmButton = new JButton("确认");

    JLabel accountLabel = new JLabel("账号");
    JLabel passLabel = new JLabel("密码");
    JLabel confirmPassLabel = new JLabel("密码");
    JTextField accountInput = new JTextField();
    JPasswordField passInput = new JPasswordField();
    JPasswordField confirmPassInput = new JPasswordField();

    public JPanel registerBoard = new JPanel();
    Font font = new Font("黑体", Font.BOLD, 15);

    public Register() {
        registerBoard.setLayout(null);
        registerBoard.setBackground(Color.white);

        accountLabel.setBounds(30, 25, 180, 30);
        accountLabel.setFont(font);
        accountInput.setBounds(110, 25, 180, 30);
        accountInput.setFont(font);

        passLabel.setBounds(30, 60, 180, 30);
        passLabel.setFont(font);
        passInput.setBounds(110, 60, 180, 30);
        passInput.setFont(font);
        passInput.setEchoChar('*');

        confirmPassLabel.setBounds(30, 95, 180, 30);
        confirmPassLabel.setFont(font);
        confirmPassInput.setBounds(110, 95, 180, 30);
        confirmPassInput.setFont(font);
        passInput.setEchoChar('*');
        confirmPassInput.setEchoChar('*');

        registerBoard.add(accountLabel);
        registerBoard.add(accountInput);
        registerBoard.add(passLabel);
        registerBoard.add(passInput);
        registerBoard.add(confirmPassLabel);
        registerBoard.add(confirmPassInput);

        cleanButton.addActionListener(new ButtonListeners(this));
        confirmButton.addActionListener(new ButtonListeners(this));
        cleanButton.setBackground(Color.green);
        cleanButton.setFont(font);
        cleanButton.setBounds(50, 140, 110, 30);
        confirmButton.setBackground(Color.green);
        confirmButton.setFont(font);
        confirmButton.setBounds(230, 140, 110, 30);
        registerBoard.add(confirmButton);
        registerBoard.add(cleanButton);
        this.add(registerBoard);
        this.setTitle("用户注册");
        this.setResizable(false);
        this.setBounds(650, 330, 380, 280);
        this.setVisible(true);
    }

    class ButtonListeners implements ActionListener {
        public Register register;

        public ButtonListeners(Register register) {
            this.register = register;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == confirmButton) {

                String password1 = String.valueOf(passInput.getPassword());
                String password2 = String.valueOf(confirmPassInput.getPassword());
                SwingActionExecutor.execute(() -> ServiceAssert.isTrue(!StrUtil.hasBlank(password1, password2), "请正确输入用户密码"));

                SwingActionExecutor.execute(() -> ServiceAssert.isTrue(password1.equals(password2), "密码不匹配！"));

                SwingActionExecutor.execute(() -> UserSupporter.register(accountInput.getText(), password1));
                setVisible(false);
            }
            if (e.getSource() == cleanButton) {
                accountInput.setText(null);
                passInput.setText(null);
                confirmPassInput.setText(null);
            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.setEnabled(false);
        this.setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
    }
}
