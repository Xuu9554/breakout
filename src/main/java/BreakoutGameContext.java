import cn.hutool.core.util.RandomUtil;
import db.BreakoutMapper;
import db.MapperExecutor;
import dto.GameSetting;
import dto.User;
import lombok.Data;

import java.applet.AudioClip;
import java.awt.*;
import java.util.List;
import java.util.*;

@Data
public class BreakoutGameContext {

    public final static Map<Color, Integer> BRICK_SCORE = new HashMap<>();

    static {
        BRICK_SCORE.put(Color.RED, 10);
        BRICK_SCORE.put(Color.GREEN, 8);
        BRICK_SCORE.put(Color.GRAY, 6);
        BRICK_SCORE.put(Color.YELLOW, 12);
        BRICK_SCORE.put(Color.ORANGE, 9);
        BRICK_SCORE.put(Color.PINK, 11);
        BRICK_SCORE.put(Color.CYAN, 7);
    }

    private static final List<Color> BRICK_COLORS = new ArrayList<>(BRICK_SCORE.keySet());

    private final User currentUser;

    private final Paddle paddle = new Paddle();

    private final Ball ball = new Ball();

    private final List<Brick> bricks = new ArrayList<>();

    private final BreakoutComponents breakoutComponents;

    private final Timer timer = new Timer();

    private final int highestScore;

    private AudioClip clip;

    private int boardWidth = 0;

    private int period = 60;

    private int score = 0;

    private int successCount = 0;

    private int remainingBrickCount = 0;

    private boolean pause = true;

    public BreakoutGameContext(User currentUser) {
        this.currentUser = currentUser;
        this.initBoardData(currentUser);
        this.breakoutComponents = new BreakoutComponents(this.paddle, this.ball, this.bricks);
        this.highestScore = MapperExecutor.query(BreakoutMapper::fetchCurrentLoggedInUserHighScore);
    }

    /**
     * 当前得分扣除指定分数
     *
     * @param score 分数
     */
    public void deductScore(int score) {
        this.score -= score;
    }

    /**
     * 当前得分增加指定分数
     *
     * @param score 分数
     */
    public void addScore(int score) {
        this.score += score;
    }

    /**
     * 剩余砖块数减一
     */
    public void decreaseRemainingBrickCount() {
        this.remainingBrickCount--;
    }

    /**
     * 切换暂停状态
     */
    public void togglePause() {
        this.pause = !this.pause;
    }

    /**
     * 初始化当前一局的砖块和游戏配置
     *
     * @param currentUser 当前登录用户
     */
    private void initBoardData(User currentUser) {

        this.bricks.clear();

        for (int count = 0; count < 100; count++) {

            Brick brick = new Brick();

            brick.setColor(RandomUtil.randomEle(BRICK_COLORS));
            this.bricks.add(brick);
        }

        this.score = 0;
        this.remainingBrickCount = 100;
        this.pause = true;
        this.ball.setVelocityX(3);
        this.ball.setVelocityY(-2);

        GameSetting gameSetting = GameSetting.from(currentUser);
        this.ball.setLife(gameSetting.getBallLife());
        this.ball.setRadius(gameSetting.getBallSize());
        this.successCount = gameSetting.getClearBrickCount();
        this.period = gameSetting.getFps();
    }

}
