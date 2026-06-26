import cn.hutool.core.util.StrUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import static javax.swing.SwingUtilities.invokeLater;

public class MainGame extends JFrame {
    private static final long serialVersionUID = -4482258986636771110L;
    public JPanel jPanel = new JPanel();
    public JButton singleGame = new JButton("单人游戏");
    public JButton gameSetting = new JButton("游戏设置");
    public JButton escapeGame = new JButton("退出游戏");
    public JButton recordList = new JButton("英雄榜");
    public JButton register = new JButton("注册");
    public JButton login = new JButton("登陆");
    public JButton loginOut = new JButton("退出登录");
    public String userId = "";
    public String userName = "";
    public JLabel helloWorld = new JLabel("", JLabel.CENTER);

    Font font = new Font("黑体", Font.BOLD, 26);

    public MainGame() {

        String _userId = GameSupporter.loadCurrentLoggedInUser();
        if (StrUtil.isBlank(_userId)) {
            register.setBounds(190, 470, 180, 60);
            register.setFont(font);
            login.setBounds(190, 550, 180, 60);
            login.setFont(font);
            register.setFocusPainted(false);
            register.setBorderPainted(false);
            login.setFocusPainted(false);
            login.setBorderPainted(false);
            register.setBackground(Color.white);
            login.setBackground(Color.white);
            jPanel.add(register);
            jPanel.add(login);
            register.addActionListener(new ButtonListeners(this));
            login.addActionListener(new ButtonListeners(this));
        } else {
            userId = String.valueOf(_userId);
            userName =_userId;
            helloWorld.setBounds(0, 470, 550, 60);
            helloWorld.setFont(font);
            String text = PM_AM() + userName;
            helloWorld.setText(text);
            jPanel.add(helloWorld);

            loginOut.setBounds(190, 550, 180, 60);
            loginOut.setFont(font);
            loginOut.setBackground(Color.red);
            loginOut.setFocusPainted(false);
            loginOut.setBorderPainted(false);
//                    loginOut.setBackground(Color.white);
            loginOut.addActionListener(new ButtonListeners(this));
            jPanel.add(loginOut);
        }

        jPanel.setLayout(null);
        singleGame.setBounds(190, 110, 180, 60);
        singleGame.setFont(font);
        gameSetting.setBounds(190, 190, 180, 60);
        gameSetting.setFont(font);
        recordList.setBounds(190, 270, 180, 60);
        recordList.setFont(font);
        escapeGame.setBounds(190, 350, 180, 60);
        escapeGame.setFont(font);
        singleGame.setFocusPainted(false);
        singleGame.setBorderPainted(false);
        gameSetting.setFocusPainted(false);
        gameSetting.setBorderPainted(false);
        escapeGame.setFocusPainted(false);
        escapeGame.setBorderPainted(false);
        recordList.setFocusPainted(false);
        recordList.setBorderPainted(false);
        singleGame.setBackground(Color.white);
        gameSetting.setBackground(Color.white);
        escapeGame.setBackground(Color.white);
        recordList.setBackground(Color.white);
        jPanel.add(singleGame);
        jPanel.add(gameSetting);
        jPanel.add(escapeGame);
        jPanel.add(recordList);
        jPanel.setBackground(Color.white);
        this.add(jPanel);
        this.setBackground(Color.white);
        this.setBounds(550, 80, 550, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle("打~砖~块");
        singleGame.addActionListener(new ButtonListeners(this));
        gameSetting.addActionListener(new ButtonListeners(this));
        escapeGame.addActionListener(new ButtonListeners(this));
        recordList.addActionListener(new ButtonListeners(this));

    }

    public String PM_AM() {
        String result = "";
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour < 6) {
            result = "清晨";
        } else if (hour >= 6 && hour < 12) {
            result = "上午";
        } else if (hour >= 12 && hour < 14) {
            result = "中午";
        } else if (hour >= 14 && hour < 18) {
            result = "下午";
        } else if (hour >= 18 && hour < 24) {
            result = "晚上";
        }
        return result + "好，";
    }

    private class ButtonListeners implements ActionListener {
        public MainGame game;

        public ButtonListeners(MainGame game) {
            this.game = game;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == singleGame) {
                if (userId.equals("")) {
                    int n = JOptionPane.showConfirmDialog(null, "请先登录！", "提示", JOptionPane.YES_NO_OPTION);
                    if (n == 0) {
                        setVisible(false);
                        new Login();
                    } else {
//                        setVisible(false);
//                        new MainGame();
                    }
                } else {
                    setVisible(false);
                    dispose();
                    invokeLater(() -> {
                        JBreakout j = null;
                        j = new JBreakout(userId, userName);
                        j.setBackground(Color.white);
                        j.setSize(550, 700);
                        j.setLocation(550, 80);
                        j.setVisible(true);
                        j.setBreakoutComponents();
                    });
                }
            }
            if (e.getSource() == gameSetting) {
                if (userId.equals("")) {
                    int n = JOptionPane.showConfirmDialog(null, "请先登录！", "提示", JOptionPane.YES_NO_OPTION);
                    if (n == 0) {
                        setVisible(false);
                        new Login();
                    }
                } else {
                    new Setting(userId);
                }
            }
            if (e.getSource() == login) {
                setVisible(false);
                new Login();
            }
            if (e.getSource() == loginOut) {
                GameSupporter.logout();
                JOptionPane.showMessageDialog(null, "退出登录成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
                new MainGame();
            }
            if (e.getSource() == register) {
                new Register();
            }
            if (e.getSource() == escapeGame) {
                System.exit(1);
            }
            if (e.getSource() == recordList) {
                if (userId.equals("")) {
                    int n = JOptionPane.showConfirmDialog(null, "请先登录！", "提示", JOptionPane.YES_NO_OPTION);
                    if (n == 0) {
                        setVisible(false);
                        new Login();
                    }
                } else {
                    new RecordList(userId, userName);
                    setVisible(false);
                }
            }
        }
    }

    public static void main(String[] args) {

        new MainGame();

    }


}
