package view;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.RandomUtil;
import dto.*;
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
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

import static ui.GameFonts.GAME_INFO_TEXT;

@Slf4j
public class BreakoutGameFrame extends JFrame implements KeyListener {

    private static final long serialVersionUID = 4655988232932265069L;

    /**
     * 游戏画布宽度
     */
    public final static int APPLICATION_WIDTH = 400;

    /**
     * 游戏画布高度
     */
    public final static int APPLICATION_HEIGHT = 545;

    /**
     * 游戏绘制组件宽度
     */
    private final static int BREAKOUT_COMPONENT_WIDTH = 550;

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
    private final static int WINDOW_WIDTH = 550;

    /**
     * 游戏窗口高度
     */
    private final static int WINDOW_HEIGHT = 700;

    /**
     * 每行砖块数量
     */
    private final static int BRICKS_PER_ROW = 10;

    /**
     * 砖块间距
     */
    private final static int BRICK_SEP = 4;

    /**
     * 小球掉落时扣除的分数
     */
    private final static int SCORE_DEDUCTED_ON_BALL_DROP = 50;

    /**
     * 破纪录背景音乐资源路径
     */
    private final static String RECORD_BREAKING_BACKGROUND_MUSIC = "/music/break.wav";

    /**
     * 背景音乐资源池
     */
    private final static List<String> NORMAL_BACKGROUND_MUSIC = Arrays.asList("/music/game1.wav", "/music/game2.wav");

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
            breakout.setBackground(Color.WHITE);
            breakout.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            breakout.setLocation(WINDOW_X, WINDOW_Y);
            breakout.setVisible(true);
            breakout.setBreakoutGameCanvas();
        });
    }

    private BreakoutGameFrame() {

        this.context = new BreakoutGameContext(GameSupporter.requireCurrentUser());

        System.out.println("小球的生命：" + this.context.getBall().getLife());
        System.out.println("小球的大小：" + this.context.getBall().getRadius());
        System.out.println("现在几帧：" + this.context.getRoundState().getPeriod());
        System.out.println("通关条件：" + this.context.getRoundState().getSuccessCount());

        this.setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
        this.setTitle("打~砖~块");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel gamePanel = new JPanel(null);
        gamePanel.setBackground(Color.WHITE);

        SwingFormFactory formFactory = SwingFormFactory.with(gamePanel, GAME_INFO_TEXT);

        formFactory.label("本机最高纪录为：", 260, 550, 200, 40);
        JLabel great = formFactory.label("", 430, 550, 40, 40, Color.RED);

        formFactory.label("小球生命剩余：", 30, 580, 150, 40);
        JLabel life = formFactory.label("", 180, 580, 40, 40, Color.RED);

        formFactory.label("当前剩余砖块：", 260, 580, 150, 40);
        JLabel brickCount = formFactory.label("", 410, 580, 40, 40, Color.RED);

        formFactory.label("页面刷新频率：", 30, 610, 150, 40);
        JLabel fps = formFactory.label("", 180, 610, 40, 40, Color.RED);

        formFactory.label("当前游戏状态：", 260, 610, 150, 40);
        JLabel status = formFactory.label("未开始", 410, 610, 130, 40, Color.RED);

        formFactory.label("您已获得:", 30, 550, 190, 40);
        JLabel hadGainScore = formFactory.label("", 130, 550, 80, 40, Color.MAGENTA);

        formFactory.component(this.context.getBreakoutGameCanvas(), 0, 0, BREAKOUT_COMPONENT_WIDTH, APPLICATION_HEIGHT);

        this.add(gamePanel);
        this.context.getBreakoutGameCanvas().addKeyListener(this);
        this.context.getBreakoutGameCanvas().setFocusable(true);

        this.switchBackgroundMusic(this.randomBackgroundMusic());

        this.gameLoop = () -> this.runGameLoop(great, life, brickCount, fps, status, hadGainScore);
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
     * @param life         生命标签
     * @param brickCount   剩余砖块数标签
     * @param fps          刷新频率标签
     * @param status       游戏状态标签
     * @param hadGainScore 当前得分标签
     */
    private void runGameLoop(JLabel great, JLabel life, JLabel brickCount, JLabel fps, JLabel status, JLabel hadGainScore) {

        BreakoutRoundState roundState = this.context.getRoundState();

        this.updateBrickWidth();
        this.context.getBreakoutGameCanvas().repaint();

        if (this.context.getBall().moveAndBounce(roundState.getBoardWidth(), APPLICATION_HEIGHT, roundState.isPause(),
                roundState.getRemainingBrickCount(), this.context.getPaddle())) {
            roundState.deductScore(SCORE_DEDUCTED_ON_BALL_DROP);
        }

        hadGainScore.setText(String.valueOf(roundState.getScore()));
        this.updateBackgroundMusicByScore(roundState);

        if (roundState.getScore() > this.context.getHighestScore()) {
            hadGainScore.setForeground(Color.BLUE);
            great.setText(String.valueOf(roundState.getScore()));
            great.setForeground(Color.BLUE);
        } else {
            hadGainScore.setForeground(Color.MAGENTA);
            great.setText(String.valueOf(this.context.getHighestScore()));
            great.setForeground(Color.RED);
        }

        brickCount.setText(String.valueOf(roundState.getRemainingBrickCount()));
        fps.setText(String.valueOf(roundState.getPeriod()));

        Ball ball = this.context.getBall();
        Paddle paddle = this.context.getPaddle();

        if (ball.getVelocityY() > 0 && ball.collide(paddle.getX(), paddle.getY(), paddle.getPaddleWidth(), paddle.getPaddleHeight())) {
            ball.setY(paddle.getY() - ball.getRadius());
            ball.reverseVelocityY();
        }

        if (this.context.getBall().isAlive()) {

            life.setText(String.valueOf(ball.getLife()));

            if (this.context.hasClearedRequiredBricks()) {
                this.context.getTimer().cancel();
                this.stopMusic();
                this.playEndingMusic("/music/win.wav");
                SwingWindows.dispose(this);

                String title = roundState.getScore() > this.context.getHighestScore() ? "恭喜您打破本机记录！" : "恭喜您成功通关！";
                new GameResultFrame(BreakoutGameResult.of(title, roundState.getScore()));
            }

        } else {
            this.context.getTimer().cancel();
            life.setText(String.valueOf(0));
            status.setText("游戏终止");
            this.stopMusic();
            this.playEndingMusic("/music/lose.wav");
            SwingDialogs.information(null, "叭好意思您失败了~", "游戏结束");
            this.context.getTimer().cancel();
            SwingWindows.hideAndOpen(this, MainMenuFrame::new);
        }

        Opt.ofNullable(ball.findFirstHitBrick(this.context.getBricks())).ifPresent(hitBrick -> {

            ball.bounceOffRectangle(hitBrick.getX(), hitBrick.getY(), hitBrick.getWidth(), Brick.HEIGHT);

            hitBrick.setAlive(false);
            roundState.decreaseRemainingBrickCount();

            roundState.addScore(BreakoutGameContext.BRICK_SCORE.getOrDefault(hitBrick.getColor(), 5));
        });

        status.setText(roundState.isPause() ? "游戏暂停" : "游戏进行中");
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
     * 根据当前分数切换背景音乐
     *
     * @param roundState 当前局状态
     */
    private void updateBackgroundMusicByScore(BreakoutRoundState roundState) {

        if (roundState.getScore() > this.context.getHighestScore()) {
            this.switchBackgroundMusic(RECORD_BREAKING_BACKGROUND_MUSIC);
        } else if (RECORD_BREAKING_BACKGROUND_MUSIC.equals(this.currentBackgroundMusicResource)) {
            this.switchBackgroundMusic(this.randomBackgroundMusic());
        }
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
     * 将设置页保存后的配置实时应用到当前局
     *
     * @param gameSetting 游戏配置
     */
    private void applyCurrentRoundSetting(GameSetting gameSetting) {
        int oldPeriod = this.context.getRoundState().getPeriod();
        this.context.applyCurrentRoundSetting(gameSetting);
        if (oldPeriod != this.context.getRoundState().getPeriod()) {
            this.context.restartTimer();
            this.scheduleGameLoop();
        }
        this.context.getBreakoutGameCanvas().repaint();
    }

    /**
     * 根据游戏面板宽度刷新砖块坐标
     */
    private void updateBrickWidth() {
        int rowIndex = 0;
        int columnIndex = 0;
        BreakoutRoundState roundState = this.context.getRoundState();
        int brickWidth = (roundState.getBoardWidth() - BRICKS_PER_ROW * BRICK_SEP) / BRICKS_PER_ROW;
        for (Brick brick : this.context.getBricks()) {

            brick.setWidth(brickWidth);
            brick.setX(columnIndex * brickWidth + BRICK_SEP * (columnIndex + 1));
            brick.setY(rowIndex * Brick.HEIGHT + BRICK_SEP * rowIndex);

            columnIndex++;
            if (columnIndex == BRICKS_PER_ROW) {
                columnIndex = 0;
                rowIndex++;
            }
        }
    }

    public void setBreakoutGameCanvas() {
        BreakoutRoundState roundState = this.context.getRoundState();
        roundState.setBoardWidth(this.getContentPane().getWidth());
        int boardHeight = this.getContentPane().getHeight();
        this.context.getPaddle().setStartPosition(roundState.getBoardWidth(), boardHeight);
    }

    @Override
    public void keyTyped(KeyEvent e) {

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
                roundState.setPause(true);
                this.context.getAudioPlayer().pause();
                new GameSettingsFrame(this::applyCurrentRoundSetting);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
