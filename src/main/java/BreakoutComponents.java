import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BreakoutComponents extends JComponent {

    /**
     * 当前局挡板
     */
    private final Paddle paddle;

    /**
     * 当前局小球
     */
    private final Ball ball;

    /**
     * 当前局砖块列表
     */
    private final List<Brick> bricks;

    public BreakoutComponents(Paddle paddle, Ball ball, List<Brick> bricks) {
        this.paddle = paddle;
        this.ball = ball;
        this.bricks = bricks;
    }

    @Override
    protected void paintComponent(Graphics g) {
        this.paddle.draw(g);
        this.ball.draw(g);
        for (Brick brick : this.bricks) {
            brick.draw(g);
        }
    }

}
