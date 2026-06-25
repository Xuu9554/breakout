import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class Login extends JFrame implements ActionListener {
    DataBase data = new DataBase();
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
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(650, 330, 380, 280);
        this.setVisible(true);
    }

    public String loginUser(String userName, String passWord) throws SQLException {
        long time1 = new Date().getTime();
        PreparedStatement pre;
        String result = "";
        String checkSql = "select * from user u where u.user_name = ?";
        pre = data.conn.prepareStatement(checkSql);
        pre.setString(1, userName);
        ResultSet res = pre.executeQuery();
        res.last();
        if (res.getRow() != 0) {
            String sql = "select * from user u where u.user_name = ? and u.pass_word = ?";
            pre = data.conn.prepareStatement(sql);
            pre.setString(1, userName);
            pre.setString(2, passWord);
            ResultSet loginResult = pre.executeQuery();
            loginResult.last();
            if (loginResult.getRow() != 0) {
                sql = "INSERT INTO user_login(login_id,user_id,login_time,expired_time) values (?,?,?,?)";
                pre = data.conn.prepareStatement(sql);
                pre.setString(1, UUID.randomUUID().toString().replace("-", ""));
                pre.setString(2, String.valueOf(loginResult.getString("user_id")));
                pre.setObject(3, new Date());
                pre.setObject(4, null);
                pre.executeUpdate();
                result = "登陆成功！";
                long time2 = new Date().getTime();
                System.out.println("本次登陆一共卡顿 " + (time2 - time1) / 1000.0 + "s");
            } else {
                result = "账号密码不匹配！";
            }
        } else {
            result = "该用户名不存在";
        }

        return result;
    }

    class ButtonListeners implements ActionListener {
        public Login login;

        public ButtonListeners(Login login) {
            this.login = login;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == confirmButton) {
                if (accountInput.getText().equals("") || passInput.getPassword().equals("")) {
                    JOptionPane.showMessageDialog(null, "内容不能为空！", "提示", JOptionPane.NO_OPTION);
                } else {
                    try {
                        String res = login.loginUser(accountInput.getText(), String.valueOf(passInput.getPassword()));
                        JOptionPane.showMessageDialog(null, res, "提示", JOptionPane.NO_OPTION);
                        if (res.equals("登陆成功！")) {
                            setVisible(false);
                            new MainGame();
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            if (e.getSource() == cleanButton) {
                setVisible(false);
                new MainGame();
            }

        }
    }

    public void actionPerformed(ActionEvent e) {
        this.setEnabled(false);
        this.setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
    }
}