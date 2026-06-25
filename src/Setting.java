import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Setting extends JFrame implements ActionListener {
    Ball ball = new Ball();
    DataBase data = new DataBase();
    public JPanel settingBoard = new JPanel();
    public JLabel ballLifeLabel = new JLabel("小球生命");
    public JLabel ballSizeLabel = new JLabel("小球大小");
    public JLabel rateLabel = new JLabel("刷新频率");
    public JLabel successCountLabel = new JLabel("通关块数");
    public static JLabel lifeWarning = new JLabel("小球生命至少为一次");
    public static JLabel successCountLabelWarning = new JLabel("建议输入0-99之间的整数");
    public JLabel integerWarning = new JLabel("建议输入8-18之间的整数");
    public static JLabel rateWarning = new JLabel("建议刷新率在60-80之间");
    public static JTextField ballLife = new JTextField();
    public static JTextField brickCount = new JTextField();
    public JTextField ballSize = new JTextField();
    public static JTextField rate = new JTextField();
    public JButton confirm = new JButton("Apply");
    Font font = new Font("黑体", Font.BOLD, 15);
    public String realUserId = "";

    public Setting(String userId) {
        realUserId = userId;
        settingBoard.setLayout(null);
        settingBoard.setBackground(Color.white);
//        Map<String, Object> dataList = getSetting(userId);
        SettingDTO dto = getSetting(userId);
        ballLifeLabel.setBounds(30, 25, 180, 30);
        ballLifeLabel.setFont(font);
        ballLife.setBounds(110, 25, 50, 30);
        ballLife.setFont(font);
        ballLife.setText(dto.getLife().toString());
        lifeWarning.setBounds(170, 25, 180, 30);
        lifeWarning.setFont(font);
        lifeWarning.setForeground(Color.red);
        settingBoard.add(ballLifeLabel);
        settingBoard.add(ballLife);
        settingBoard.add(lifeWarning);

        ballSizeLabel.setBounds(30, 60, 180, 30);
        ballSizeLabel.setFont(font);
        ballSize.setBounds(110, 60, 50, 30);
        ballSize.setFont(font);
        ballSize.setText(dto.getSize().toString());
        integerWarning.setBounds(170, 60, 180, 30);
        integerWarning.setFont(font);
        integerWarning.setForeground(Color.red);
        settingBoard.add(ballSizeLabel);
        settingBoard.add(ballSize);
        settingBoard.add(integerWarning);

        rateLabel.setBounds(30, 95, 180, 30);
        rateLabel.setFont(font);
        rate.setBounds(110, 95, 50, 30);
        rate.setFont(font);
        rate.setText(dto.getPeriod().toString());
        rateWarning.setBounds(170, 95, 180, 30);
        rateWarning.setFont(font);
        rateWarning.setForeground(Color.red);
        settingBoard.add(rateLabel);
        settingBoard.add(rate);
        settingBoard.add(rateWarning);

        successCountLabel.setBounds(30, 130, 180, 30);
        successCountLabel.setFont(font);
        brickCount.setBounds(110, 130, 50, 30);
        brickCount.setFont(font);
        brickCount.setText(dto.getBrickCount().toString());
        successCountLabelWarning.setBounds(170, 130, 180, 30);
        successCountLabelWarning.setFont(font);
        successCountLabelWarning.setForeground(Color.red);
        settingBoard.add(successCountLabel);
        settingBoard.add(brickCount);
        settingBoard.add(successCountLabelWarning);

        confirm.setBackground(Color.green);
        confirm.setFont(font);
        confirm.setBounds(230, 180, 110, 30);
        settingBoard.add(confirm);
        confirm.addActionListener(new ButtonListeners(this));

        this.add(settingBoard);
        this.setTitle("Game Setting");
        this.setResizable(false);
        this.setBounds(650, 330, 380, 280);
        this.setVisible(true);
    }


    public SettingDTO getSetting(String userId) {
        SettingDTO dto = new SettingDTO();
        PreparedStatement pre;
        try {
            String checkSql = "select * from setting_record s where s.user_id=? limit 1";
            pre = data.conn.prepareStatement(checkSql);
            pre.setString(1, userId);
            ResultSet res = pre.executeQuery();
            res.last();
            dto.setUserId(String.valueOf(res.getString("user_id")))
                    .setLife(Integer.valueOf(res.getString("ball_life")))
                    .setPeriod(Integer.valueOf(res.getString("period")))
                    .setBrickCount(Integer.valueOf(res.getString("brick_count")))
                    .setSize(Integer.valueOf(res.getString("ball_size")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dto;
    }


    public void updateSetting(SettingDTO settingDTO) {
        PreparedStatement pre;
        try {
            String updateSql = "update setting_record set ball_life=?,ball_size=?,period=?,brick_count=? where user_id=?";
            pre = data.conn.prepareStatement(updateSql);
            pre.setInt(1, settingDTO.getLife());
            pre.setInt(2, settingDTO.getSize());
            pre.setInt(3, settingDTO.getPeriod());
            pre.setInt(4, settingDTO.getBrickCount());
            pre.setString(5, settingDTO.getUserId());
            pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    class ButtonListeners implements ActionListener {
        public Setting setting;

        public ButtonListeners(Setting setting) {
            this.setting = setting;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == confirm) {
                SettingDTO dto = getSetting(realUserId);
                if (ballLife.getText().matches("[0-9]*") && rate.getText().matches("[0-9]*") && ballSize.getText().matches("[0-9]*") && brickCount.getText().matches("[0-9]*")) {
                    int realLife = Integer.parseInt(ballLife.getText());
                    int realRadius = Integer.parseInt(ballSize.getText());
                    int realPeriod = Integer.parseInt(rate.getText());
                    int realBrick = Integer.parseInt(brickCount.getText());
                    if (realLife <= 0) realLife = 1;
                    if (realRadius <= 0) realRadius = 1;
                    if (realRadius >= 18) realRadius = 18;
                    if (realPeriod >= 100) realPeriod = 100;
                    if (realBrick > 99) realBrick = 99;
                    if (realBrick <= 0) realBrick = 0;
                    if (realLife == dto.getLife() &&
                            realRadius == dto.getSize() &&
                            realPeriod == dto.getPeriod() &&
                            realBrick == dto.getBrickCount()) {
                        System.out.println("没有任何修改！");
                        JOptionPane.showMessageDialog(null, "没有任何修改！", "提示", JOptionPane.NO_OPTION);
                    } else {
                        Ball.setLife(realLife);
                        Ball.setBallRadius(realRadius);
                        JBreakout.setPeriod(realPeriod);
                        JBreakout.setSuccessCount(realBrick);
                        System.out.println("生命：" + realLife +
                                " 小球大小：" + realRadius +
                                " 刷新频率：" + realPeriod +
                                " 通关块数：" + realBrick);
                        SettingDTO settingDTO = new SettingDTO();
                        settingDTO.setSize(realRadius)
                                .setBrickCount(realBrick)
                                .setPeriod(realPeriod)
                                .setLife(realLife)
                                .setUserId(realUserId);
                        updateSetting(settingDTO);
                        System.out.println("修改成功！");
                        JOptionPane.showMessageDialog(null,
                                "修改参数成功！" + '\n'
                                        + "生命：" + realLife + '\n'
                                        + "小球大小：" + realRadius + '\n'
                                        + "刷新频率：" + realPeriod + '\n'
                                        + "通关块数：" + realBrick,
                                "操作成功！", JOptionPane.NO_OPTION);
                        this.setting.setVisible(false);
                    }

                } else
                    JOptionPane.showMessageDialog(null, "您刚才输入的值不合法！", "错误", JOptionPane.NO_OPTION);
            }

        }
    }

    public void actionPerformed(ActionEvent e) {
        this.setEnabled(false);
        this.setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
    }
}
