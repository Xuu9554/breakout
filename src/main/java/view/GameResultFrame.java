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

        this.openWindow(GameWindowConfig
                .of(gameResult.getTitle(), WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT)
                .setBackground(PANEL_BACKGROUND).setCloseOperation(WindowConstants.EXIT_ON_CLOSE));
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

        JLabel title = formFactory.label(this.gameResult.getTitle(), 0, 30, WINDOW_WIDTH, 42, TITLE_COLOR);
        title.setFont(RECORD_SCORE);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        formFactory.label("您的大名：", 106, 104, 110, 30, LABEL_TEXT_COLOR);
        formFactory.label(currentUser.getUserId(), 224, 104, 180, 30, LABEL_TEXT_COLOR).setFont(RECORD_TEXT);

        formFactory.label("您的成绩：", 106, 150, 110, 30, LABEL_TEXT_COLOR);
        JLabel scoreLabel = formFactory.label(String.valueOf(gameResult.getScore()), 224, 142, 180, 42, TITLE_COLOR);
        scoreLabel.setFont(RECORD_SCORE);

        JButton confirmButton = formFactory.button(new ArcadeMenuButton("确定"), 190, 218, 120, 40, BUTTON_BLUE);
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(RECORD_TEXT);
        confirmButton.setFocusPainted(false);
        confirmButton.setBorderPainted(false);

        SwingActionFactory.with(this).bind(confirmButton, () -> this.confirmRecord(currentUser, this.gameResult));
    }

    /**
     * 确认并保存当前成绩
     */
    private void confirmRecord(User currentUser, BreakoutGameResult gameResult) {
        Integer highScore = currentUser.getHighScore();
        if (ObjectUtil.isNull(highScore) || gameResult.getScore() > highScore) {
            MapperExecutor.execute(mapper -> mapper.updateHighScore(currentUser.getUserId(), gameResult.getScore()));
        }
        LeaderboardFrame.open(this);
    }

    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 成绩确认窗口横坐标
     */
    private final static int WINDOW_X = 650;

    /**
     * 成绩确认窗口纵坐标
     */
    private final static int WINDOW_Y = 315;

    /**
     * 成绩确认窗口宽度
     */
    private final static int WINDOW_WIDTH = 500;

    /**
     * 成绩确认窗口高度
     */
    private final static int WINDOW_HEIGHT = 300;

    /**
     * 成绩确认页背景色
     */
    private final static Color PANEL_BACKGROUND = new Color(248, 250, 252);

    /**
     * 成绩确认页标题颜色
     */
    private final static Color TITLE_COLOR = new Color(42, 107, 255);

    /**
     * 成绩确认页标签颜色
     */
    private final static Color LABEL_TEXT_COLOR = new Color(38, 50, 56);

    /**
     * 成绩确认页按钮颜色
     */
    private final static Color BUTTON_BLUE = new Color(42, 107, 255);

}
