import support.UserSupporter;
import ui.SwingActionExecutor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1504087619736129919L;

    JButton cleanButton = new JButton("回到主界面");
    JButton confirmButton = new JButton("确认");

    JLabel accountLabel = new JLabel("账号");
    JLabel passLabel = new JLabel("密码");
    JTextField accountInput = new JTextField();
    JPasswordField passInput = new JPasswordField();

    public JPanel loginBoard = new JPanel();
    Font font = new Font("黑体", Font.BOLD, 15);

    public Login() {
        loginBoard.setLayout(null);
        loginBoard.setBackground(Color.white);

        accountLabel.setBounds(30, 25, 180, 30);
        accountLabel.setFont(font);
        accountInput.setBounds(110, 25, 180, 30);
        accountInput.setFont(font);

        passLabel.setBounds(30, 60, 180, 30);
        passLabel.setFont(font);
        passInput.setBounds(110, 60, 180, 30);
        passInput.setFont(font);
        passInput.setEchoChar('*');

        cleanButton.addActionListener(new ButtonListeners(this));
        confirmButton.addActionListener(new ButtonListeners(this));
        cleanButton.setBackground(Color.green);
        cleanButton.setFont(font);
        cleanButton.setBounds(50, 140, 150, 30);
        confirmButton.setBackground(Color.green);
        confirmButton.setFont(font);
        confirmButton.setBounds(230, 140, 110, 30);
        loginBoard.add(accountLabel);
        loginBoard.add(accountInput);
        loginBoard.add(passInput);
        loginBoard.add(passLabel);

        loginBoard.add(confirmButton);
        loginBoard.add(cleanButton);
        this.add(loginBoard);
        this.setTitle("用户登录");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setBounds(650, 330, 380, 280);
        this.setVisible(true);
    }

    class ButtonListeners implements ActionListener {
        public Login login;

        public ButtonListeners(Login login) {
            this.login = login;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == confirmButton) {
                SwingActionExecutor.execute(()->UserSupporter.login(accountInput.getText(), String.valueOf(passInput.getPassword())));
                setVisible(false);
                new MainGame();
            }
            if (e.getSource() == cleanButton) {
                setVisible(false);
                new MainGame();
            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.setEnabled(false);
        this.setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
    }
}