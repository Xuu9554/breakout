import cn.hutool.core.util.RandomUtil;
import db.BreakoutMapper;
import db.MapperExecutor;
import dto.GameSetting;
import dto.User;
import lombok.Data;
import ui.GameAudioPlayer;

import java.awt.*;
import java.util.List;
import java.util.*;

@Data
public class BreakoutGameContext {

    /**
     * 当前登录用户
     */
    private final User currentUser;

    /**
     * 当前局挡板
     */
    private final Paddle paddle;

    /**
     * 当前局小球
     */
    private final Ball ball;

    /**
     * 当前局所有砖块
     */
    private final List<Brick> bricks;

    /**
     * 游戏绘制组件
     */
    private final BreakoutComponents breakoutComponents;

    /**
     * 当前局刷新计时器
     */
    private Timer timer;

    /**
     * 当前用户历史最高分
     */
    private final int highestScore;

    /**
     * 当前局运行状态
     */
    private final BreakoutRoundState roundState;

    /**
     * 当前正在播放的音频
     */
    private final GameAudioPlayer audioPlayer;

    /**
     * 创建当前登录用户的一局游戏上下文
     *
     * @param currentUser 当前登录用户
     */
    public BreakoutGameContext(User currentUser) {

        this.paddle = new Paddle();
        this.ball = new Ball();
        this.bricks = this.createBricks();
        this.timer = new Timer();
        this.roundState = new BreakoutRoundState();
        this.audioPlayer = new GameAudioPlayer();

        this.breakoutComponents = new BreakoutComponents(this.paddle, this.ball, this.bricks);
        this.highestScore = MapperExecutor.query(BreakoutMapper::fetchCurrentLoggedInUserHighScore);

        this.roundState.reset(BRICK_COUNT);
        this.ball.setVelocityX(3);
        this.ball.setVelocityY(-2);

        this.currentUser = currentUser;
        this.applyGameSetting(GameSetting.from(currentUser));
    }

    /**
     * 判断是否已经达到通关所需消除块数
     *
     * @return boolean 是否通关
     */
    public boolean hasClearedRequiredBricks() {
        return this.roundState.hasClearedRequiredBricks(BRICK_COUNT);
    }

    /**
     * 应用当前局游戏配置
     *
     * @param gameSetting 游戏配置
     */
    public void applyCurrentRoundSetting(GameSetting gameSetting) {
        this.applyGameSetting(gameSetting);
    }

    /**
     * 重启当前局刷新计时器
     */
    public void restartTimer() {
        this.timer.cancel();
        this.timer = new Timer();
    }

    /**
     * 创建当前一局的初始砖块
     *
     * @return {@link List<Brick>} 初始砖块列表
     */
    private List<Brick> createBricks() {
        List<Brick> initialBricks = new ArrayList<>();
        for (int count = 0; count < BRICK_COUNT; count++) {
            Brick brick = new Brick();
            brick.setColor(RandomUtil.randomEle(BRICK_COLORS));
            initialBricks.add(brick);
        }
        return initialBricks;
    }

    /**
     * 应用当前用户的游戏配置
     *
     * @param gameSetting 游戏配置
     */
    private void applyGameSetting(GameSetting gameSetting) {
        this.ball.setLife(gameSetting.getBallLife());
        this.ball.setRadius(gameSetting.getBallSize());
        this.roundState.applyGameSetting(gameSetting, BRICK_COUNT);
    }

    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 不同颜色砖块对应的分值
     */
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

    /**
     * 每局初始砖块总数
     */
    public final static int BRICK_COUNT = 100;

    /**
     * 砖块可选颜色池
     */
    private final static List<Color> BRICK_COLORS = new ArrayList<>(BRICK_SCORE.keySet());

}
