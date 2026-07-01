package view;

import cn.hutool.core.util.ObjectUtil;
import db.MapperExecutor;
import dto.BreakoutGameResult;
import dto.User;
import support.GameSupporter;
import ui.*;

import javax.swing.*;
import java.awt.*;

import static ui.GameFonts.RECORD_SCORE;
import static ui.GameFonts.RECORD_TEXT;

public class GameResultFrame extends AbstractGameFrame {

    private static final long serialVersionUID = 2306704671104481919L;

    private final BreakoutGameResult gameResult;

    public GameResultFrame(BreakoutGameResult gameResult) {
        this.gameResult = gameResult;
        this.openWindow(GameWindowConfig.of(gameResult.getTitle(), 650, 330, 380, 230).setCloseOperation(WindowConstants.EXIT_ON_CLOSE));
    }

    /**
     * 构建成绩确认内容
     *
     * @param panel 当前窗口的根面板
     */
    @Override
    protected void buildContent(JPanel panel) {
        User currentUser = GameSupporter.requireCurrentUser();
        SwingFormFactory formFactory = SwingFormFactory.with(panel, RECORD_TEXT);

        formFactory.label("您的大名：", 30, 25, 180, 30);
        formFactory.label(currentUser.getUserId(), 170, 25, 150, 30).setFont(RECORD_TEXT);

        formFactory.label("您的成绩：", 30, 80, 180, 30);
        JLabel scoreLabel = formFactory.label(String.valueOf(gameResult.getScore()), 170, 80, 180, 30, Color.RED);
        scoreLabel.setFont(RECORD_SCORE);

        SwingActionFactory.with(this)
                .bind(formFactory.button("确定", 230, 140, 110, 30, Color.GREEN), () -> this.confirmRecord(currentUser, this.gameResult));
    }

    /**
     * 确认并保存当前成绩
     */
    private void confirmRecord(User currentUser, BreakoutGameResult gameResult) {
        this.saveRecord(currentUser, gameResult);
        SwingWindows.hideAndOpen(this, LeaderboardFrame::new);
    }

    /**
     * 保存当前最高成绩
     *
     * @param currentUser 当前登录用户
     * @param gameResult  游戏结果
     */
    private void saveRecord(User currentUser, BreakoutGameResult gameResult) {
        Integer highScore = currentUser.getHighScore();
        if (ObjectUtil.isNull(highScore) || gameResult.getScore() > highScore) {
            MapperExecutor.execute(mapper -> mapper.updateHighScore(currentUser.getUserId(), gameResult.getScore()));
        }
    }

}
