import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class Record extends JFrame {
    DataBase data = new DataBase();
    public JPanel jPanel = new JPanel();
    public JLabel userNameLabel = new JLabel("您的大名：");
    public JLabel userNameInput = new JLabel("");
    public JLabel scoreLabel = new JLabel("您的成绩：");
    public JLabel score = new JLabel(String.valueOf(JBreakout.score));
    //    public JTextField userNameInput = new JTextField("Visitor");
    public JButton confirm = new JButton("确定");
    //public JButton button2=new JButton("取消");
    Font font = new Font("黑体", Font.BOLD, 16);
    Font font2 = new Font("黑体", Font.BOLD, 30);
    String user_id = "";

    public Record(String title, String userId, String userName) {
        user_id = userId;
        jPanel.setLayout(null);
        jPanel.setBackground(Color.white);

        userNameLabel.setBounds(30, 25, 180, 30);
        userNameLabel.setFont(font);
        jPanel.add(userNameLabel);

        userNameInput.setBounds(170, 25, 150, 30);
        userNameInput.setFont(font);
        userNameInput.setText(userName);
        jPanel.add(userNameInput);

        scoreLabel.setBounds(30, 80, 180, 30);
        scoreLabel.setFont(font);
        jPanel.add(scoreLabel);

        score.setBounds(170, 80, 180, 30);
        score.setFont(font2);
        score.setForeground(Color.red);
        jPanel.add(score);

        confirm.setBackground(Color.green);
        confirm.setFont(font);
        confirm.setBounds(230, 140, 110, 30);
        confirm.addActionListener(new ButtonListeners(this));
        jPanel.add(confirm);


        this.add(jPanel);

        this.setTitle(title);


        //this.setResizable(false);
        this.setBounds(650, 330, 380, 230);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public void insertData() throws SQLException {
        PreparedStatement pre;
        int realScore = JBreakout.score;
        String checkSql = "select * from record where record.user_id=? order by record.grade desc limit 1";
        pre = DataBase.conn.prepareStatement(checkSql);
        pre.setString(1, user_id);
        ResultSet res = pre.executeQuery();
        res.last();
        int grade = res.getRow() == 0 ? 0 : res.getInt("grade");
        if (res.getRow() == 0 /*|| realScore < grade*/) {
            System.out.println("insert!");
            String recordSql = "INSERT INTO record(record_id,user_id,grade,create_time) values (?,?,?,?)";
            pre = DataBase.conn.prepareStatement(recordSql);
            pre.setString(1, UUID.randomUUID().toString().replace("-", ""));//jdbc操作自增id貌似有点麻烦，这里暂时使用uuid
            pre.setString(2, user_id);
            pre.setInt(3, realScore);
            pre.setObject(4, new Date());
            pre.executeUpdate();
        } else {
            if (realScore > grade) {
                String recordSql = "UPDATE record SET grade=?,create_time=? WHERE user_id=?";
                pre = DataBase.conn.prepareStatement(recordSql);
                pre.setInt(1, realScore);
                pre.setObject(2, new Date());
                pre.setString(3, user_id);
                pre.executeUpdate();
            }
        }


    }

    private class ButtonListeners implements ActionListener {
        public Record record;

        public ButtonListeners(Record record) {
            this.record = record;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == confirm) {
                System.out.println("姓名：" + userNameInput.getText() + "分数：" + JBreakout.score);
                try {
                    record.insertData();
                    JOptionPane.showMessageDialog(null, "操作成功！", "提示", JOptionPane.NO_OPTION);
                    record.setVisible(false);
                    new RecordList(user_id, userNameInput.getText());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
//                System.exit(1);
            }

        }
    }
}
