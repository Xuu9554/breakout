import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class Register extends JFrame implements ActionListener {
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

    public String registerUser(String userName, String passWord) throws SQLException {
        PreparedStatement pre;
        String result = "";
        String checkSql = "select * from user u where u.user_name = ?";
        pre = DataBase.conn.prepareStatement(checkSql);
        pre.setString(1, userName);
        ResultSet res = pre.executeQuery();
        res.last();
        if (res.getRow() == 0) {
            String sql = "INSERT INTO user(user_id,user_name,pass_word,create_time) values (?,?,?,?)";
            pre = DataBase.conn.prepareStatement(sql);
            String userId = UUID.randomUUID().toString().replace("-", "");
            pre.setString(1, userId);
            pre.setString(2, userName);
            pre.setString(3, passWord);
            pre.setObject(4, new Date());
            pre.executeUpdate();
            result = "注册成功！";
            sql = "INSERT INTO setting_record(setting_id, user_id, ball_life, ball_size, period, brick_count) VALUES (?,?,?,?,?,?)";
            pre = DataBase.conn.prepareStatement(sql);
            pre.setString(1, UUID.randomUUID().toString().replace("-", ""));
            pre.setString(2, userId);
            pre.setInt(3, 1);
            pre.setInt(4, 10);
            pre.setInt(5, 60);
            pre.setInt(6, 0);
            pre.executeUpdate();
        } else {
            result = "该用户名已被注册！";
        }
        return result;
    }

    class ButtonListeners implements ActionListener {
        public Register register;

        public ButtonListeners(Register register) {
            this.register = register;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == confirmButton) {
                if (String.valueOf(accountInput.getText()).equals("")
                        || String.valueOf(passInput.getPassword()).equals("")
                        || String.valueOf(confirmPassInput.getPassword()).equals("")) {
                    JOptionPane.showMessageDialog(null, "内容不能为空！", "提示", JOptionPane.NO_OPTION);
                } else {
                    if (String.valueOf(passInput.getPassword()).equals(String.valueOf(confirmPassInput.getPassword()))) {
                        try {
                            String res = register.registerUser(accountInput.getText(), String.valueOf(confirmPassInput.getPassword()));
                            JOptionPane.showMessageDialog(null, res, "提示", JOptionPane.NO_OPTION);
                            if (res.equals("注册成功！")) {
                                setVisible(false);
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "密码不匹配！", "提示", JOptionPane.NO_OPTION);
                    }
                }
            }
            if (e.getSource() == cleanButton) {
                accountInput.setText(null);
                passInput.setText(null);
                confirmPassInput.setText(null);
            }

        }
    }

    public void actionPerformed(ActionEvent e) {
        this.setEnabled(false);
        this.setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
    }
}
