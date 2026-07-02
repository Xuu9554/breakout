package support;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import db.BreakoutMapper;
import db.MapperExecutor;
import dto.*;
import lombok.Data;
import ui.GameAudioPlayer;
import view.BreakoutGameCanvas;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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
     * 当前局所有小球
     */
    private final List<Ball> balls;

    /**
     * 当前局所有砖块
     */
    private final List<Brick> bricks;

    /**
     * 游戏绘制组件
     */
    private final BreakoutGameCanvas breakoutGameCanvas;

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

        this.balls = new CopyOnWriteArrayList<>();

        Ball start = new Ball();
        start.setVelocityX(3);
        start.setVelocityY(-2);
        this.balls.add(start);

        this.timer = new Timer();
        this.roundState = new BreakoutRoundState();
        this.audioPlayer = new GameAudioPlayer();

        this.bricks = new ArrayList<>();

        for (int count = 0; count < BRICK_COUNT; count++) {

            Brick brick = new Brick();
            Color color = RandomUtil.randomEle(BRICK_COLORS);
            brick.setColor(color);
            brick.setWidth(BRICK_WIDTH);
            brick.setLife(this.getBrickRule(color).getLife());
            brick.setX(BRICK_AREA_X + (count % BRICKS_PER_ROW) * (BRICK_WIDTH + BRICK_SEP));
            brick.setY(BRICK_AREA_Y + (count / BRICKS_PER_ROW) * (Brick.HEIGHT + BRICK_SEP));
            this.bricks.add(brick);
        }

        this.breakoutGameCanvas = new BreakoutGameCanvas(this.paddle, this.balls, this.bricks);
        this.highestScore = MapperExecutor.query(BreakoutMapper::fetchCurrentLoggedInUserHighScore);

        this.roundState.reset(BRICK_COUNT);

        this.currentUser = currentUser;

        GameSetting gameSetting = GameSetting.from(currentUser);
        this.balls.forEach(ball -> {
            ball.setLife(gameSetting.getBallLife());
            ball.setRadius(gameSetting.getBallSize());
        });

        this.roundState.applyGameSetting(gameSetting, BRICK_COUNT);
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
     * 获取主球
     *
     * @return {@link Ball} 主球
     */
    public Ball getPrimaryBall() {
        return CollUtil.getFirst(this.balls);
    }

    /**
     * 判断当前局是否还有存活小球
     *
     * @return boolean 是否还有存活小球
     */
    public boolean hasAliveBall() {
        return this.balls.stream().anyMatch(Ball::isAlive);
    }

    /**
     * 根据挡板位置重置初始小球位置
     */
    public void resetInitialBallPosition() {
        this.getPrimaryBall().setStartPosition(this.paddle);
    }

    /**
     * 尝试按当前分数复制小球
     *
     * @param parent 发生碰撞的祖球
     */
    public void splitBallIfNecessary(Ball parent) {

        while (this.roundState.getScore() >= BALL_SPLIT_SCORE_INTERVAL * this.balls.size() && this.balls.size() < MAX_BALL_COUNT) {

            Ball ball = new Ball();
            ball.setAlive(true);
            ball.setLife(parent.getLife());
            ball.setRadius(parent.getRadius());
            ball.setX(parent.getX());
            ball.setY(parent.getY());
            ball.setPreviousX(parent.getX());
            ball.setPreviousY(parent.getY());
            ball.setVelocityX(-parent.getVelocityX());
            ball.setVelocityY(parent.getVelocityY());
            this.balls.add(ball);
        }
    }

    /**
     * 获取指定颜色的砖块规则
     *
     * @param color 砖块颜色
     * @return {@link BrickRule} 砖块规则
     */
    public BrickRule getBrickRule(Color color) {
        return BRICK_RULES.getOrDefault(color, DEFAULT_BRICK_RULE);
    }

    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 每行砖块数量
     */
    public final static int BRICKS_PER_ROW = 10;

    /**
     * 砖块行数
     */
    public final static int BRICK_ROW_COUNT = 10;

    /**
     * 每局初始砖块总数
     */
    public final static int BRICK_COUNT = BRICKS_PER_ROW * BRICK_ROW_COUNT;

    /**
     * 砖块宽度
     */
    public final static int BRICK_WIDTH = 71;

    /**
     * 砖块布局区域横坐标
     */
    public final static int BRICK_AREA_X = 8;

    /**
     * 砖块布局区域纵坐标
     */
    public final static int BRICK_AREA_Y = 20;

    /**
     * 小球复制触发分数间隔
     */
    public final static int BALL_SPLIT_SCORE_INTERVAL = 50;

    /**
     * 小球掉落时扣除的分数
     */
    public final static int SCORE_DEDUCTED_ON_BALL_DROP = 50;

    /**
     * 当前局最多小球数量
     */
    public final static int MAX_BALL_COUNT = 1;

    /**
     * 是否展示砖块生命
     */
    public final static boolean SHOW_LIFE = true;

    /**
     * 砖块布局间距
     */
    private final static int BRICK_SEP = 4;

    /**
     * 默认砖块规则
     */
    private final static BrickRule DEFAULT_BRICK_RULE = BrickRule.of(5, 1);

    /**
     * 红色砖块
     */
    private final static Color BRICK_RED = new Color(211, 47, 47);

    /**
     * 橙色砖块
     */
    private final static Color BRICK_ORANGE = new Color(230, 81, 0);

    /**
     * 黄色砖块
     */
    private final static Color BRICK_YELLOW = new Color(156, 111, 0);

    /**
     * 绿色砖块
     */
    private final static Color BRICK_GREEN = new Color(46, 125, 50);

    /**
     * 青色砖块
     */
    private final static Color BRICK_CYAN = new Color(0, 131, 143);

    /**
     * 蓝色砖块
     */
    private final static Color BRICK_BLUE = new Color(25, 118, 210);

    /**
     * 紫色砖块
     */
    private final static Color BRICK_PURPLE = new Color(123, 31, 162);

    /**
     * 不同颜色砖块对应的规则
     */
    public final static Map<Color, BrickRule> BRICK_RULES = new LinkedHashMap<>();

    static {
        BRICK_RULES.put(BRICK_RED, BrickRule.of(10, 2));
        BRICK_RULES.put(BRICK_ORANGE, BrickRule.of(9, 7));
        BRICK_RULES.put(BRICK_YELLOW, BrickRule.of(12, 3));
        BRICK_RULES.put(BRICK_GREEN, BrickRule.of(8, 4));
        BRICK_RULES.put(BRICK_CYAN, BrickRule.of(7, 3));
        BRICK_RULES.put(BRICK_BLUE, BrickRule.of(6, 1));
        BRICK_RULES.put(BRICK_PURPLE, BrickRule.of(11, 2));
    }

    /**
     * 砖块可选颜色池
     */
    private final static List<Color> BRICK_COLORS = new ArrayList<>(BRICK_RULES.keySet());

}
