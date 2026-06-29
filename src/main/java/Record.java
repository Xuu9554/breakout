import db.MapperExecutor;
import dto.BreakoutGameResult;
import dto.User;
import ui.SwingActionFactory;
import ui.SwingFormFactory;

import javax.swing.*;
import java.awt.*;

public class Record extends JFrame {

    private static final long serialVersionUID = 2306704671104481919L;

    public Record(BreakoutGameResult gameResult) {

        User currentUser = GameSupporter.requireCurrentUser();

        JPanel recordPanel = new JPanel();
        recordPanel.setLayout(null);
        recordPanel.setBackground(Color.WHITE);

        Font textFont = new Font("黑体", Font.BOLD, 16);
        SwingFormFactory formFactory = SwingFormFactory.with(recordPanel, textFont);

        formFactory.label("您的大名：", 30, 25, 180, 30);
        formFactory.label(currentUser.getUserId(), 170, 25, 150, 30).setFont(textFont);

        formFactory.label("您的成绩：", 30, 80, 180, 30);
        JLabel scoreLabel = formFactory.label(String.valueOf(gameResult.getScore()), 170, 80, 180, 30, Color.RED);
        scoreLabel.setFont(new Font("黑体", Font.BOLD, 30));

        SwingActionFactory.with(this).bind(formFactory.button("确定", 230, 140, 110, 30, Color.GREEN),
                () -> this.confirmRecord(currentUser, gameResult));

        this.add(recordPanel);
        this.setTitle(gameResult.getTitle());
        this.setBounds(650, 330, 380, 230);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * 确认并保存当前成绩
     */
    private void confirmRecord(User currentUser, BreakoutGameResult gameResult) {
        this.saveRecord(currentUser, gameResult);
        JOptionPane.showMessageDialog(this, "操作成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        this.setVisible(false);
        new RecordList();
    }

    /**
     * 保存当前最高成绩
     *
     * @param currentUser 当前登录用户
     * @param gameResult  游戏结果
     */
    private void saveRecord(User currentUser, BreakoutGameResult gameResult) {
        Integer highScore = currentUser.getHighScore();
        if (highScore == null || gameResult.getScore() > highScore) {
            MapperExecutor.execute(mapper -> mapper.updateHighScore(currentUser.getUserId(), gameResult.getScore()));
        }
    }
}
