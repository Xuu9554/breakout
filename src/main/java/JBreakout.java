import db.BreakoutMapper;
import db.MapperExecutor;
import dto.GameSetting;

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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class JBreakout extends JFrame implements KeyListener {
    private static final long serialVersionUID = 4655988232932265069L;

    int theGreatGrade;

    //窗体的大小
    public static final int APPLICATION_WIDTH = 400;
    public static final int APPLICATION_HEIGHT = 545;

    //游戏面板实际大小
    public static int Width = 0;
    public static int Height = 0;
    public static boolean pause = true;
    public static int count = 1;
    //每层砖块的数量

    public static int Period = 60;
    private static final int BRICKS_PER_ROW = 10;
    //层数
    private static final int BRICK_ROWS = 10;
    //砖块之间的间隔
    private static final int BRICK_SEP = 4;
    Paddle paddle = new Paddle();
    Ball ball = new Ball();
    JLabel hadGain = new JLabel("您已获得:");
    JLabel hadGainScore = new JLabel("");
    JLabel greatLabel = new JLabel("本机最高纪录为：");
    JLabel great = new JLabel("");
    JLabel lifeLabel = new JLabel("小球生命剩余：");
    JLabel life = new JLabel("");
    JLabel brickCountLabel = new JLabel("当前剩余砖块：");
    JLabel brickCount = new JLabel("");
    JLabel rateLabel = new JLabel("页面刷新频率：");
    JLabel rate = new JLabel("");
    JLabel statusLabel = new JLabel("当前游戏状态：");
    JLabel status = new JLabel("未开始");
    Font bigFont = new Font("TimesRoman", Font.BOLD, 20);
    Font smallFont = new Font("TimesRoman", Font.BOLD, 14);
    public static int score = 0;
    public String userRealId = "";
    public static int successCount = 0;
    static ArrayList<Brick> bricks = initBricks();
    public static int brickscount = bricks.size();
    BreakoutComponents breakoutComponents;
    AudioClip clip;
    String winMusic = "music\\win.wav";
    String loseMusic = "music\\lose.wav";
    String backGroundMusic = "music\\game.wav";
    String backGroundMusicTmp = "music\\break.wav";
    String happyMusic = "music\\happyMusic.wav";
    static Timer timer;

    public static void setPeriod(int period) {
        JBreakout.Period = period;
    }

    public static void setSuccessCount(int successCount) {
        JBreakout.successCount = successCount;
    }

    public static int getSuccessCount() {
        return successCount;
    }

    public JBreakout(String userId, String userName) {

        this.userRealId = userId;

        initBoardData(userId);
        breakoutComponents = new BreakoutComponents(paddle, ball, bricks);
        System.out.println("小球的生命：" + ball.getLife());
        System.out.println("小球的大小：" + ball.getBallRadius());
        timer = new Timer();
        System.out.println("现在几帧：" + this.Period);
        System.out.println("通关条件：" + this.getSuccessCount());

        File winMusicFile = new File(winMusic);
        File loseMusicFile = new File(loseMusic);

        ArrayList<String> musicFileList = new ArrayList<>();
        musicFileList.add(backGroundMusic);
        musicFileList.add(backGroundMusicTmp);
        musicFileList.add(happyMusic);

        Random random = new Random();
        int musicIndex = random.nextInt(3);
        String musicFileName = musicFileList.get(musicIndex);
        System.out.println("当前播放音乐：" + musicFileName);

        File backGroundMusicFile = new File(musicFileName);
        //设置窗体的大小
        // music.playMusic();
        setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
        //设置窗体标题
        setTitle("打~砖~块");
        this.setResizable(false);
        //设置窗体不可改变

        //设置右上角关闭按钮
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //添加组件
        add(greatLabel);
        greatLabel.setBounds(260, 550, 200, 40);
        greatLabel.setFont(bigFont);

        add(great);
        great.setBounds(430, 550, 40, 40);
        great.setFont(bigFont);
        great.setForeground(Color.red);

        add(lifeLabel);
        lifeLabel.setBounds(30, 580, 150, 40);
        lifeLabel.setFont(bigFont);

        add(life);
        life.setBounds(180, 580, 40, 40);
        life.setFont(bigFont);
        life.setForeground(Color.red);

        add(brickCountLabel);
        brickCountLabel.setBounds(260, 580, 150, 40);
        brickCountLabel.setFont(bigFont);

        add(brickCount);
        brickCount.setBounds(410, 580, 40, 40);
        brickCount.setFont(bigFont);
        brickCount.setForeground(Color.red);

        add(rateLabel);
        rateLabel.setBounds(30, 610, 150, 40);
        rateLabel.setFont(bigFont);
        //  rateLabel.setForeground(Color.red);

        add(rate);
        rate.setBounds(180, 610, 40, 40);
        rate.setFont(bigFont);
        rate.setForeground(Color.red);

        add(statusLabel);
        statusLabel.setBounds(260, 610, 150, 40);
        statusLabel.setFont(bigFont);

        add(status);
        status.setBounds(410, 610, 130, 40);
        status.setFont(bigFont);
        status.setForeground(Color.red);

        add(hadGain);
        hadGain.setBounds(30, 550, 190, 40);
        hadGain.setFont(bigFont);

        add(hadGainScore);
        hadGainScore.setBounds(130, 550, 80, 40);
        hadGainScore.setFont(bigFont);
        hadGainScore.setForeground(Color.magenta);

        add(breakoutComponents);
        //设置监听事件
        breakoutComponents.addKeyListener(this);
        //设置组件为焦点
        breakoutComponents.setFocusable(true);

//        musicObject.playMusic(backGroundMusic, 2);//游戏循环播放音乐
        int finalStatus = 1;
        URI bgmUrI = backGroundMusicFile.toURI();
        try {
            URL url = bgmUrI.toURL();
            clip = Applet.newAudioClip(url);
            clip.loop();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        timer.schedule(new TimerTask() {
                           @Override
                           public void run() {

                               updateBrickWidth();
                               breakoutComponents.repaint();
                               ball.moveAndBounce(score);
                               hadGainScore.setText(String.valueOf(score));

                               if (score > theGreatGrade) {
                                   hadGainScore.setForeground(Color.BLUE);
                                   great.setText(String.valueOf(score));
                                   great.setForeground(Color.BLUE);

                               } else {
                                   hadGainScore.setForeground(Color.magenta);
                                   great.setText(String.valueOf(theGreatGrade));
                                   great.setForeground(Color.red);
                               }
                               brickCount.setText(String.valueOf(brickscount));
                               rate.setText(String.valueOf(Period));
                               //判断是否和paddle碰撞
                               if (ball.collide(paddle.getX(), paddle.getY(), Paddle.drawPADDLE_WIDTH, Paddle.drawPADDLE_HEIGHT)) {
                                   ball.rebounce_paddle();
                               }
                               if (ball.isGame_isAlive()) {
                                   life.setText(String.valueOf(ball.life));
                                   if (brickscount < successCount + 1) {
                                       dispose();
                                       timer.cancel();
                                       clip.stop();
                                       try {
                                           clip = Applet.newAudioClip(winMusicFile.toURI().toURL());
                                           clip.play();
                                       } catch (MalformedURLException e) {
                                           JOptionPane.showMessageDialog(null, "播放音乐时遇到了错误！", "提示", JOptionPane.NO_OPTION);
                                           System.out.println("播放错误~");
                                       }
                                       setVisible(false);
                                       String title = "";
                                       if (score > theGreatGrade) {
                                           title = "恭喜您打破本机记录！";
                                       } else {
                                           title = "恭喜您成功通关！";
                                       }
                                       new Record(title, userId, userName);

                                   }
                               } else {
                                   timer.cancel();
                                   life.setText(String.valueOf(0));
                                   status.setText("游戏终止");
                                   clip.stop();
                                   try {
                                       clip = Applet.newAudioClip(loseMusicFile.toURI().toURL());
                                       clip.play();
                                   } catch (MalformedURLException e) {
                                       JOptionPane.showMessageDialog(null, "播放音乐时遇到了错误！", "提示", JOptionPane.NO_OPTION);
                                       System.out.println("播放错误~");
                                   }
                                   JOptionPane.showMessageDialog(null, "叭好意思您失败了~", "游戏结束", JOptionPane.NO_OPTION);
//                                   System.exit(1);
                                   setVisible(false);
                                   timer.cancel();
                                   new MainGame();
                               }

                               for (Brick brick : bricks) {
                                   if (brick.isAlive() && ball.collide(brick.getX(), brick.getY(), brick.getBRICK_WIDTH(), Brick.BRICK_HEIGHT)) {
                                       ball.rebounceY();
                                       brick.setAlive(false);
                                       if (!brick.isAlive()) {
                                           brickscount--;
                                       }
                                       if (brick.getColor() == Color.red) {
                                           score += 10;
                                       }
                                       if (brick.getColor() == Color.green) {
                                           score += 8;
                                       }
                                       if (brick.getColor() == Color.gray) {
                                           score += 6;
                                       }
                                       if (brick.getColor() == Color.yellow) {
                                           score += 12;
                                       }
                                       if (brick.getColor() == Color.orange) {
                                           score += 9;
                                       }
                                       if (brick.getColor() == Color.pink) {
                                           score += 11;
                                       }
                                       if (brick.getColor() == Color.cyan) {
                                           score += 7;
                                       }
                                   }
                               }
                               if (pause == false) {
                                   status.setText("游戏进行中");
                               } else {
                                   status.setText("游戏暂停");
                               }
                           }

                       },
                0, 1000 / Period);
        try {
            Thebest();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void Thebest() throws SQLException {

        int userHighScore = MapperExecutor.query(BreakoutMapper::fetchCurrentLoggedInUserHighScore);

        theGreatGrade = userHighScore;
        great.setText(String.valueOf(userHighScore));

    }

    private void updateBrickWidth() {
        //i,j用于记录砖块的位置，j是横排的序号，i的纵排的序号
        int i = 0, j = 0;
        int BRICK_WIDTH = (JBreakout.Width - (BRICKS_PER_ROW) * BRICK_SEP) / BRICKS_PER_ROW;
        for (Brick brick : bricks) {
            brick.setBRICK_WIDTH(BRICK_WIDTH);
            //x,y计算砖块坐标
            int x = j * BRICK_WIDTH + 4 * (j + 1);
            brick.setX(x);
            int y = i * Brick.BRICK_HEIGHT + 4 * i;
            brick.setY(y);
            j++;
            if (j == 10) {
                j = 0;
                i++;
            }
        }
    }

    private static ArrayList<Brick> initBricks() {
        //实例化一个list
        Color Colorful[] = {Color.RED, Color.ORANGE, Color.GREEN, Color.GRAY, Color.YELLOW, Color.PINK, Color.CYAN};
        //int score=0;
        ArrayList<Brick> bricks = new ArrayList<Brick>();
        //通过双重for循环，将所有brick实例化后添加到list中
        //通过switch语句给砖块添加颜色
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICKS_PER_ROW; j++) {
                int index = (int) (Math.random() * Colorful.length);
                Color rand = Colorful[index];
                Brick brick = new Brick();
                brick.setColor(rand);
                bricks.add(brick);

            }
        }
        return bricks;
    }

    public void initBoardData(String userId) {
        //实例化一个list
        Color Colorful[] = {Color.RED, Color.ORANGE, Color.GREEN, Color.GRAY, Color.YELLOW, Color.PINK, Color.CYAN};
        //int score=0;
        ArrayList<Brick> bricks = new ArrayList<Brick>();
        //通过双重for循环，将所有brick实例化后添加到list中
        //通过switch语句给砖块添加颜色
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICKS_PER_ROW; j++) {
                int index = (int) (Math.random() * Colorful.length);
                Color rand = Colorful[index];
                Brick brick = new Brick();
                brick.setColor(rand);
                bricks.add(brick);

            }
        }
        JBreakout.bricks = bricks;
        JBreakout.score = 0;
        JBreakout.brickscount = bricks.size();
        JBreakout.pause = true;
        ball.setVx(3);
        ball.setVy(-2);
        GameSetting dto = GameSupporter.loadGameSetting(userId);
        System.out.println(dto);
        ball.setLife(dto.getBallLife());
        JBreakout.setSuccessCount(dto.getClearBrickCount());
        JBreakout.setPeriod(dto.getFps());
        ball.setBallRadius(dto.getBallSize());
    }

    public void setBreakoutComponents() {
        Width = this.getContentPane().getWidth();
        Height = this.getContentPane().getHeight();
        paddle.setStartPosition();

        System.out.println("Width：" + Width + "      Height：" + Height + " " + bricks.size());
        System.out.println("Paddle的初始位置 (" + paddle.getX() + "，" + paddle.getY() + ")  宽：" + paddle.getPaddleHeight() + " 长：" + paddle.getPaddleWidth());
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                paddle.moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                paddle.moveRight();
                break;
            case KeyEvent.VK_SPACE:
                if (!pause)//暂停
                {
                    pause = true;
                } else//开始
                {
                    pause = false;
                }
                break;
            case KeyEvent.VK_ESCAPE:
                pause = true;
                new Setting(userRealId);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
