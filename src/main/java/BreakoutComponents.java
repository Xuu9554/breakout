import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BreakoutComponents extends JComponent {
    Paddle paddle;
    Ball ball;
    ArrayList<Brick> bricks;

    public BreakoutComponents(Paddle paddle,Ball ball,ArrayList<Brick> bricks){

        this.paddle = paddle;
        this.ball = ball;
        this.bricks = bricks;
    }

    @Override
    protected void paintComponent(Graphics g) {
        paddle.draw(g);
        ball.draw(g);
        for (Brick brick : bricks){
            brick.draw(g);
        }
    }
}