import cn.hutool.core.lang.Opt;
import dto.BreakoutGameResult;
import dto.GameSetting;
import ui.SwingActionExecutor;
import ui.SwingFormFactory;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import static ui.GameFonts.GAME_INFO_TEXT;

public class JBreakout extends JFrame implements KeyListener {

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
     * 胜利音效路径
     */
    private final static String WIN_MUSIC_PATH = "music\\win.wav";

    /**
     * 失败音效路径
     */
    private final static String LOSE_MUSIC_PATH = "music\\lose.wav";

    /**
     * 游戏背景音乐路径
     */
    private final static String BACKGROUND_MUSIC_PATH = "music\\game.wav";

    /**
     * 砖块音效背景音乐路径
     */
    private final static String BREAK_MUSIC_PATH = "music\\break.wav";

    /**
     * 轻快背景音乐路径
     */
    private final static String HAPPY_MUSIC_PATH = "music\\happyMusic.wav";

    /**
     * 当前游戏上下文
     */
    private final BreakoutGameContext context;

    /**
     * 当前游戏循环
     */
    private final Runnable gameLoop;

    /**
     * 打开当前登录用户的游戏窗口
     *
     * @param parent 父组件
     */
    public static void open(Component parent) {
        SwingActionExecutor.execute(parent, () -> {
            JBreakout breakout = new JBreakout();
            breakout.setBackground(Color.WHITE);
            breakout.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            breakout.setLocation(WINDOW_X, WINDOW_Y);
            breakout.setVisible(true);
            breakout.setBreakoutComponents();
        });
    }

    private JBreakout() {

        this.context = new BreakoutGameContext(GameSupporter.requireCurrentUser());

        System.out.println("小球的生命：" + this.context.getBall().getLife());
        System.out.println("小球的大小：" + this.context.getBall().getRadius());
        System.out.println("现在几帧：" + this.context.getRoundState().getPeriod());
        System.out.println("通关条件：" + this.context.getRoundState().getSuccessCount());

        File winMusicFile = new File(WIN_MUSIC_PATH);
        File loseMusicFile = new File(LOSE_MUSIC_PATH);
        File backgroundMusicFile = new File(this.randomBackgroundMusicPath());

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

        formFactory.component(this.context.getBreakoutComponents(), 0, 0, BREAKOUT_COMPONENT_WIDTH, APPLICATION_HEIGHT);

        this.add(gamePanel);
        this.context.getBreakoutComponents().addKeyListener(this);
        this.context.getBreakoutComponents().setFocusable(true);

        this.playBackgroundMusic(backgroundMusicFile);

        this.gameLoop = () -> this.runGameLoop(great, life, brickCount, fps, status, hadGainScore, winMusicFile, loseMusicFile);
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
                JBreakout.this.gameLoop.run();
            }

        }, 0, 1000 / this.context.getRoundState().getPeriod());
    }

    /**
     * 执行一帧游戏刷新
     *
     * @param great         最高分标签
     * @param life          生命标签
     * @param brickCount    剩余砖块数标签
     * @param fps           刷新频率标签
     * @param status        游戏状态标签
     * @param hadGainScore  当前得分标签
     * @param winMusicFile  胜利音乐文件
     * @param loseMusicFile 失败音乐文件
     */
    private void runGameLoop(JLabel great, JLabel life, JLabel brickCount, JLabel fps, JLabel status,
                             JLabel hadGainScore, File winMusicFile, File loseMusicFile) {

        BreakoutRoundState roundState = this.context.getRoundState();

        this.updateBrickWidth();
        this.context.getBreakoutComponents().repaint();

        if (this.context.getBall().moveAndBounce(roundState.getBoardWidth(), APPLICATION_HEIGHT, roundState.isPause(),
                roundState.getRemainingBrickCount(), this.context.getPaddle())) {
            roundState.deductScore(SCORE_DEDUCTED_ON_BALL_DROP);
        }

        hadGainScore.setText(String.valueOf(roundState.getScore()));

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
        if (ball.getVelocityY() > 0 && ball.collide(
                paddle.getX(), paddle.getY(),
                paddle.getPaddleWidth(), paddle.getPaddleHeight())) {

            ball.setY(paddle.getY() - ball.getRadius());
            ball.reverseVelocityY();
        }

        if (this.context.getBall().isAlive()) {

            life.setText(String.valueOf(ball.getLife()));

            if (this.context.hasClearedRequiredBricks()) {
                this.dispose();
                this.context.getTimer().cancel();
                this.stopMusic();
                this.playEndingMusic(winMusicFile);
                this.setVisible(false);

                String title = roundState.getScore() > this.context.getHighestScore() ? "恭喜您打破本机记录！" : "恭喜您成功通关！";
                new Record(BreakoutGameResult.of(title, roundState.getScore()));
            }

        } else {
            this.context.getTimer().cancel();
            life.setText(String.valueOf(0));
            status.setText("游戏终止");
            this.stopMusic();
            this.playEndingMusic(loseMusicFile);
            JOptionPane.showMessageDialog(null, "叭好意思您失败了~", "游戏结束", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
            this.context.getTimer().cancel();
            new MainGame();
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
     * 随机选择一首游戏背景音乐
     *
     * @return {@link String} 背景音乐路径
     */
    private String randomBackgroundMusicPath() {

        List<String> musicFileList = new ArrayList<>();
        musicFileList.add(BACKGROUND_MUSIC_PATH);
        musicFileList.add(BREAK_MUSIC_PATH);
        musicFileList.add(HAPPY_MUSIC_PATH);

        String musicFileName = musicFileList.get(new Random().nextInt(musicFileList.size()));
        System.out.println("当前播放音乐：" + musicFileName);
        return musicFileName;
    }

    /**
     * 播放背景音乐
     *
     * @param musicFile 音乐文件
     */
    private void playBackgroundMusic(File musicFile) {
        URI bgmUri = musicFile.toURI();
        try {
            URL url = bgmUri.toURL();
            this.context.setClip(Applet.newAudioClip(url));
            this.context.getClip().loop();
        } catch (MalformedURLException exception) {
            JOptionPane.showMessageDialog(null, "播放音乐时遇到了错误！", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * 播放游戏结束音乐
     *
     * @param musicFile 音乐文件
     */
    private void playEndingMusic(File musicFile) {
        try {
            this.context.setClip(Applet.newAudioClip(musicFile.toURI().toURL()));
            this.context.getClip().play();
        } catch (MalformedURLException exception) {
            JOptionPane.showMessageDialog(null, "播放音乐时遇到了错误！", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * 停止当前播放的音乐
     */
    private void stopMusic() {
        Opt.ofNullable(this.context.getClip()).ifPresent(AudioClip::stop);
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
        this.context.getBreakoutComponents().repaint();
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

    public void setBreakoutComponents() {
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
                break;
            case KeyEvent.VK_ESCAPE:
                roundState.setPause(true);
                new Setting(this::applyCurrentRoundSetting);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
