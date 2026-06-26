import cn.hutool.core.util.ObjectUtil;
import db.MapperExecutor;
import dto.User;
import ui.SwingActionFactory;
import ui.SwingFormFactory;

import javax.swing.*;
import java.awt.*;

public class Record extends JFrame {

    private static final long serialVersionUID = 2306704671104481919L;

    private final String userId;

    private final String userName;

    private final int currentScore;

    public Record(String title, String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.currentScore = JBreakout.score;

        JPanel recordPanel = new JPanel();
        recordPanel.setLayout(null);
        recordPanel.setBackground(Color.WHITE);

        Font textFont = new Font("黑体", Font.BOLD, 16);
        SwingFormFactory formFactory = SwingFormFactory.with(recordPanel, textFont);

        formFactory.label("您的大名：", 30, 25, 180, 30);
        formFactory.label(this.userName, 170, 25, 150, 30).setFont(textFont);

        formFactory.label("您的成绩：", 30, 80, 180, 30);
        JLabel scoreLabel = formFactory.label(String.valueOf(this.currentScore), 170, 80, 180, 30);
        scoreLabel.setFont(new Font("黑体", Font.BOLD, 30));
        scoreLabel.setForeground(Color.RED);

        SwingActionFactory.with(this).bind(formFactory.button("确定", 230, 140, 110, 30, Color.GREEN), this::confirmRecord);

        this.add(recordPanel);
        this.setTitle(title);
        this.setBounds(650, 330, 380, 230);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * 确认并保存当前成绩
     */
    private void confirmRecord() {
        this.saveRecord();
        JOptionPane.showMessageDialog(this, "操作成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        this.setVisible(false);
        new RecordList(this.userId, this.userName);
    }

    /**
     * 保存当前最高成绩
     */
    private void saveRecord() {

        User user;
        if (ObjectUtil.isNull(user = GameSupporter.fetchUser(userId))) {
            return;
        }

        Integer highScore = user.getHighScore();
        if (ObjectUtil.isNull(highScore) || this.currentScore > highScore) {
            MapperExecutor.execute(mapper -> mapper.updateHighScore(this.userId, this.currentScore));
        }
    }
}
