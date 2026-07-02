package view;

import dto.Ball;
import dto.Brick;
import dto.Paddle;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BreakoutGameCanvas extends JComponent {

    private static final long serialVersionUID = -2271405512668523043L;

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
     * 创建游戏画布
     *
     * @param paddle 当前局挡板
     * @param balls  当前局所有小球
     * @param bricks 当前局所有砖块
     */
    public BreakoutGameCanvas(Paddle paddle, List<Ball> balls, List<Brick> bricks) {
        this.paddle = paddle;
        this.balls = balls;
        this.bricks = bricks;
        this.setOpaque(false);
    }

    /**
     * 绘制游戏画布
     *
     * @param graphics 图形上下文
     */
    @Override
    protected void paintComponent(Graphics graphics) {

        super.paintComponent(graphics);

        Graphics2D canvas = (Graphics2D) graphics.create();
        canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        canvas.setPaint(new GradientPaint(0, 0, BACKGROUND_START_COLOR, 0, this.getHeight(), BACKGROUND_END_COLOR));
        canvas.fillRect(0, 0, this.getWidth(), this.getHeight());

        canvas.setColor(GRID_LINE_COLOR);
        for (int x = 0; x < this.getWidth(); x += GRID_SIZE) {
            canvas.drawLine(x, 0, x, this.getHeight());
        }
        for (int y = 0; y < this.getHeight(); y += GRID_SIZE) {
            canvas.drawLine(0, y, this.getWidth(), y);
        }

        this.paddle.draw(canvas);

        new ArrayList<>(this.balls).forEach(ball -> ball.draw(canvas));
        new ArrayList<>(this.bricks).forEach(brick -> brick.draw(canvas));

        canvas.dispose();
    }

    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 游戏画布网格间距
     */
    private final static int GRID_SIZE = 40;

    /**
     * 游戏画布背景开始色
     */
    private final static Color BACKGROUND_START_COLOR = new Color(11, 20, 38);

    /**
     * 游戏画布背景结束色
     */
    private final static Color BACKGROUND_END_COLOR = new Color(31, 12, 55);

    /**
     * 游戏画布网格线颜色
     */
    private final static Color GRID_LINE_COLOR = new Color(255, 255, 255, 18);

}
