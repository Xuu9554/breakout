import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BreakoutComponents extends JComponent {

    private final Paddle paddle;

    private final Ball ball;

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
