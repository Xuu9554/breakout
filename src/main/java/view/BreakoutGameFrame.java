package view;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import dto.Ball;
import dto.BreakoutGameResult;
import dto.Brick;
import dto.Paddle;
import lombok.extern.slf4j.Slf4j;
import support.BreakoutGameContext;
import support.BreakoutRoundState;
import support.GameSupporter;
import ui.SwingActionExecutor;
import ui.SwingDialogs;
import ui.SwingFormFactory;
import ui.SwingWindows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

import static support.BreakoutGameContext.BRICK_COUNT;
import static support.BreakoutGameContext.SCORE_DEDUCTED_ON_BALL_DROP;

@Slf4j
public class BreakoutGameFrame extends JFrame implements KeyListener {

    private static final long serialVersionUID = 4655988232932265069L;

    /**
     * 当前游戏上下文
     */
    private final BreakoutGameContext context;

    /**
     * 当前游戏循环
     */
    private final Runnable gameLoop;

    /**
     * 当前正在循环播放的背景音乐资源路径
     */
    private String currentBackgroundMusicResource;

    /**
     * 打开当前登录用户的游戏窗口
     *
     * @param parent 父组件
     */
    public static void open(Component parent) {
        SwingActionExecutor.execute(parent, () -> {
            BreakoutGameFrame breakout = new BreakoutGameFrame();
            breakout.setBackground(GAME_PANEL_BACKGROUND);
            breakout.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            breakout.setLocation(WINDOW_X, WINDOW_Y);
            breakout.setVisible(true);
            breakout.setBreakoutGameCanvas();
        });
    }

    private BreakoutGameFrame() {

        this.context = new BreakoutGameContext(GameSupporter.requireCurrentUser());

        System.out.println("小球的生命：" + this.context.getPrimaryBall().getLife());
        System.out.println("小球的大小：" + this.context.getPrimaryBall().getRadius());
        System.out.println("现在几帧：" + this.context.getRoundState().getPeriod());
        System.out.println("通关条件：" + this.context.getRoundState().getSuccessCount());

        this.setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
        this.setTitle("打~砖~块");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel gamePanel = new JPanel(null);
        gamePanel.setBackground(GAME_PANEL_BACKGROUND);

        SwingFormFactory formFactory = SwingFormFactory.with(gamePanel, GAME_STATUS_TEXT);

        formFactory.label("本机最高：", RIGHT_INFO_LABEL_X, INFO_FIRST_ROW_Y, INFO_LABEL_WIDTH, 40, GAME_INFO_LABEL_COLOR);
        JLabel great = formFactory.label("", RIGHT_INFO_VALUE_X, INFO_FIRST_ROW_Y, INFO_VALUE_WIDTH, 40, GAME_HIGHEST_SCORE_COLOR);

        formFactory.label("游戏状态：", LEFT_INFO_LABEL_X, INFO_SECOND_ROW_Y, INFO_LABEL_WIDTH, 40, GAME_INFO_LABEL_COLOR);
        JLabel status = formFactory.label("未开始", LEFT_INFO_VALUE_X, INFO_SECOND_ROW_Y, INFO_VALUE_WIDTH, 40, GAME_STATUS_COLOR);

        formFactory.label("剩余砖块：", RIGHT_INFO_LABEL_X, INFO_SECOND_ROW_Y, INFO_LABEL_WIDTH, 40, GAME_INFO_LABEL_COLOR);
        JLabel brickCount = formFactory.label("", RIGHT_INFO_VALUE_X, INFO_SECOND_ROW_Y, INFO_VALUE_WIDTH, 40, GAME_BRICK_COUNT_COLOR);

        formFactory.label("当前得分：", LEFT_INFO_LABEL_X, INFO_FIRST_ROW_Y, INFO_LABEL_WIDTH, 40, GAME_INFO_LABEL_COLOR);
        JLabel hadGainScore = formFactory.label("", LEFT_INFO_VALUE_X, INFO_FIRST_ROW_Y, INFO_VALUE_WIDTH, 40, GAME_SCORE_COLOR);

        formFactory.component(this.context.getBreakoutGameCanvas(), 0, 0, BREAKOUT_COMPONENT_WIDTH, APPLICATION_HEIGHT);

        this.add(gamePanel);
        this.context.getBreakoutGameCanvas().addKeyListener(this);
        this.context.getBreakoutGameCanvas().setFocusable(true);

        this.switchBackgroundMusic(this.randomBackgroundMusic());

        this.gameLoop = () -> this.runGameLoop(great, brickCount, status, hadGainScore);
        this.scheduleGameLoop();

        great.setText(String.valueOf(this.context.getHighestScore()));
    }

    /**
     * 按当前刷新频率启动游戏循环
     */
    private void scheduleGameLoop() {

        this.context.getTimer().schedule(new TimerTask() {

            @Override
            public void run() {
                BreakoutGameFrame.this.gameLoop.run();
            }

        }, 0, 1000 / this.context.getRoundState().getPeriod());
    }

    /**
     * 执行一帧游戏刷新
     *
     * @param great        最高分标签
     * @param brickCount   剩余砖块数标签
     * @param status       游戏状态标签
     * @param hadGainScore 当前得分标签
     */
    private void runGameLoop(JLabel great, JLabel brickCount, JLabel status, JLabel hadGainScore) {

        BreakoutRoundState roundState = this.context.getRoundState();

        this.context.getBreakoutGameCanvas().repaint();

        Paddle paddle = this.context.getPaddle();
        paddle.adjustByClearedBrickProgress(roundState.getRemainingBrickCount(), BRICK_COUNT, roundState.getBoardWidth());

        List<Ball> currentBalls = new ArrayList<>(this.context.getBalls());
        List<Ball> aliveBalls = this.moveBallsAndBouncePaddle(currentBalls, roundState, paddle);

        this.collideBallsAndHitBricks(aliveBalls, roundState);

        hadGainScore.setText(String.valueOf(roundState.getScore()));

        boolean recordBreaking;
        if (recordBreaking = roundState.getScore() > this.context.getHighestScore()) {
            this.switchBackgroundMusic(RECORD_BREAKING_BACKGROUND_MUSIC);
        } else if (RECORD_BREAKING_BACKGROUND_MUSIC.equals(this.currentBackgroundMusicResource)) {
            this.switchBackgroundMusic(this.randomBackgroundMusic());
        }

        hadGainScore.setForeground(recordBreaking ? GAME_HIGHEST_SCORE_COLOR : GAME_SCORE_COLOR);
        great.setText(String.valueOf(recordBreaking ? roundState.getScore() : this.context.getHighestScore()));
        great.setForeground(GAME_HIGHEST_SCORE_COLOR);

        brickCount.setText(String.valueOf(roundState.getRemainingBrickCount()));

        if (this.context.hasAliveBall()) {

            if (this.context.hasClearedRequiredBricks()) {
                this.context.getTimer().cancel();
                this.stopMusic();
                this.playEndingMusic("/music/win.wav");
                SwingWindows.dispose(this);

                String title = roundState.getScore() > this.context.getHighestScore() ? "恭喜您打破本机记录！" : "恭喜您成功通关！";
                new GameResultFrame(BreakoutGameResult.of(title, roundState.getScore()));
            }
            status.setText(roundState.isPause() ? "游戏暂停" : "游戏进行中");
        } else {
            this.context.getTimer().cancel();
            status.setText("游戏终止");
            this.stopMusic();
            this.playEndingMusic("/music/lose.wav");
            SwingDialogs.information(null, "叭好意思您失败了~", "游戏结束");
            this.context.getTimer().cancel();
            SwingWindows.hideAndOpen(this, MainMenuFrame::new);
        }
    }

    /**
     * 移动小球并处理挡板碰撞
     *
     * @param balls      当前帧小球快照
     * @param roundState 当前局状态
     * @param paddle     当前局挡板
     * @return {@link List} 当前帧存活小球快照
     */
    private List<Ball> moveBallsAndBouncePaddle(List<Ball> balls, BreakoutRoundState roundState, Paddle paddle) {

        List<Ball> aliveBalls = new ArrayList<>();

        for (Ball ball : balls) {

            if (!ball.isAlive()) {
                continue;
            }

            if (ball.moveAndBounce(roundState.getBoardWidth(), APPLICATION_HEIGHT, roundState.isPause(), roundState.getRemainingBrickCount(), BRICK_COUNT)) {
                roundState.deductScore(SCORE_DEDUCTED_ON_BALL_DROP);
            }

            if (!ball.isAlive()) {
                continue;
            }

            if (ball.collide(paddle.getX(), paddle.getY(), paddle.getPaddleWidth(), paddle.getPaddleHeight())) {
                ball.bounceOffRectangle(paddle.getX(), paddle.getY(), paddle.getPaddleWidth(), paddle.getPaddleHeight());
            }

            aliveBalls.add(ball);
        }

        return aliveBalls;
    }

    /**
     * 处理小球碰撞和砖块命中
     *
     * @param balls      当前帧存活小球快照
     * @param roundState 当前局状态
     */
    private void collideBallsAndHitBricks(List<Ball> balls, BreakoutRoundState roundState) {

        for (int firstIndex = 0; firstIndex < balls.size(); firstIndex++) {

            Ball ball = balls.get(firstIndex);
            if (!ball.isAlive()) {
                continue;
            }

            for (int secondIndex = firstIndex + 1; secondIndex < balls.size(); secondIndex++) {

                Ball secondBall = balls.get(secondIndex);
                if (secondBall.isAlive()) {
                    ball.bounceOffBall(secondBall);
                }

            }

            Brick hitBrick;
            if (ObjectUtil.isNull(hitBrick = ball.findFirstHitBrick(this.context.getBricks()))) {
                continue;
            }

            ball.bounceOffRectangle(hitBrick.getX(), hitBrick.getY(), hitBrick.getWidth(), Brick.HEIGHT);

            roundState.addScore(this.context.getBrickRule(hitBrick.getColor()).getScore());
            this.context.splitBallIfNecessary(ball);

            if (hitBrick.hit()) {
                roundState.decreaseRemainingBrickCount();
            }
        }
    }

    /**
     * 随机选择一首普通背景音乐资源
     *
     * @return {@link String} 普通背景音乐资源路径
     */
    private String randomBackgroundMusic() {
        return RandomUtil.randomEle(NORMAL_BACKGROUND_MUSIC);
    }

    /**
     * 切换背景音乐
     *
     * @param musicResource 音乐资源路径
     */
    private void switchBackgroundMusic(String musicResource) {

        if (musicResource.equals(this.currentBackgroundMusicResource)) {
            return;
        }

        this.currentBackgroundMusicResource = musicResource;
        this.playBackgroundMusic(musicResource);

        if (this.context.getRoundState().isPause()) {
            this.context.getAudioPlayer().pause();
        }
    }

    /**
     * 播放背景音乐
     *
     * @param musicResource 音乐资源路径
     */
    private void playBackgroundMusic(String musicResource) {
        try {
            this.context.getAudioPlayer().loop(musicResource);
        } catch (Exception e) {
            log.error("尝试播放背景音乐时遇到错误: {}", e.getMessage(), e);
        }
    }

    /**
     * 播放游戏结束音乐
     *
     * @param musicResource 音乐资源路径
     */
    private void playEndingMusic(String musicResource) {
        try {
            this.context.getAudioPlayer().playOnce(musicResource);
        } catch (Exception e) {
            log.error("尝试播放游戏结束音乐时遇到错误: {}", e.getMessage(), e);
        }
    }

    /**
     * 停止当前播放的音乐
     */
    private void stopMusic() {
        this.context.getAudioPlayer().stop();
    }

    /**
     * 按当前暂停状态切换背景音乐
     */
    private void toggleBackgroundMusic() {
        if (this.context.getRoundState().isPause()) {
            this.context.getAudioPlayer().pause();
        } else {
            this.context.getAudioPlayer().resume();
        }
    }

    /**
     * 询问是否退出当前游戏局
     */
    private void confirmExitCurrentRound() {

        BreakoutRoundState roundState = this.context.getRoundState();
        boolean shouldResumeGame = !roundState.isPause();

        roundState.setPause(true);
        this.context.getAudioPlayer().pause();

        if (SwingDialogs.confirm(this, "确定退出当前游戏局吗？")) {
            this.context.getTimer().cancel();
            this.stopMusic();
            SwingWindows.hideAndOpen(this, MainMenuFrame::new);
            return;
        }

        if (shouldResumeGame) {
            roundState.setPause(false);
            this.context.getAudioPlayer().resume();
        }
    }

    /**
     * 设置游戏画布边界和挡板初始位置
     */
    public void setBreakoutGameCanvas() {
        BreakoutRoundState roundState = this.context.getRoundState();
        roundState.setBoardWidth(APPLICATION_WIDTH);
        this.context.getPaddle().setStartPosition(roundState.getBoardWidth(), APPLICATION_HEIGHT);
        this.context.resetInitialBallPosition();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        BreakoutRoundState roundState = this.context.getRoundState();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                this.context.getPaddle().moveLeft(roundState.isPause());
                break;
            case KeyEvent.VK_RIGHT:
                this.context.getPaddle().moveRight(roundState.getBoardWidth(), roundState.isPause());
                break;
            case KeyEvent.VK_SPACE:
                roundState.togglePause();
                this.toggleBackgroundMusic();
                break;
            case KeyEvent.VK_ESCAPE:
                this.confirmExitCurrentRound();
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 游戏画布宽度
     */
    public final static int APPLICATION_WIDTH = 760;

    /**
     * 游戏画布高度
     */
    public final static int APPLICATION_HEIGHT = 600;

    /**
     * 游戏绘制组件宽度
     */
    private final static int BREAKOUT_COMPONENT_WIDTH = APPLICATION_WIDTH;

    /**
     * 游戏窗口横坐标
     */
    private final static int WINDOW_X = 550;

    /**
     * 游戏窗口纵坐标
     */
    private final static int WINDOW_Y = 80;

    /**
     * 游戏窗口宽度
     */
    private final static int WINDOW_WIDTH = 800;

    /**
     * 游戏窗口高度
     */
    private final static int WINDOW_HEIGHT = 820;

    /**
     * 游戏信息第一行纵坐标
     */
    private final static int INFO_FIRST_ROW_Y = APPLICATION_HEIGHT + 20;

    /**
     * 游戏信息第二行纵坐标
     */
    private final static int INFO_SECOND_ROW_Y = APPLICATION_HEIGHT + 58;

    /**
     * 左侧信息标题横坐标
     */
    private final static int LEFT_INFO_LABEL_X = 92;

    /**
     * 左侧信息值横坐标
     */
    private final static int LEFT_INFO_VALUE_X = 238;

    /**
     * 右侧信息标题横坐标
     */
    private final static int RIGHT_INFO_LABEL_X = 382;

    /**
     * 右侧信息值横坐标
     */
    private final static int RIGHT_INFO_VALUE_X = 538;

    /**
     * 信息标题宽度
     */
    private final static int INFO_LABEL_WIDTH = 150;

    /**
     * 信息值宽度
     */
    private final static int INFO_VALUE_WIDTH = 180;

    /**
     * 游戏信息字体
     */
    private final static Font GAME_STATUS_TEXT = new Font("黑体", Font.BOLD, 24);

    /**
     * 游戏窗口背景色
     */
    private final static Color GAME_PANEL_BACKGROUND = new Color(11, 20, 38);

    /**
     * 游戏信息标题颜色
     */
    private final static Color GAME_INFO_LABEL_COLOR = new Color(207, 216, 220);

    /**
     * 游戏得分颜色
     */
    private final static Color GAME_SCORE_COLOR = new Color(64, 196, 255);

    /**
     * 游戏最高分颜色
     */
    private final static Color GAME_HIGHEST_SCORE_COLOR = new Color(255, 213, 79);

    /**
     * 游戏状态颜色
     */
    private final static Color GAME_STATUS_COLOR = new Color(129, 199, 132);

    /**
     * 游戏剩余砖块颜色
     */
    private final static Color GAME_BRICK_COUNT_COLOR = new Color(128, 216, 255);

    /**
     * 破纪录背景音乐资源路径
     */
    private final static String RECORD_BREAKING_BACKGROUND_MUSIC = "/music/break.wav";

    /**
     * 背景音乐资源池
     */
    private final static List<String> NORMAL_BACKGROUND_MUSIC = Arrays.asList("/music/game1.wav", "/music/game2.wav");

}
