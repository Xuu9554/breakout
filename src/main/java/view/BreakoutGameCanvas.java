package view;

import dto.Ball;
import dto.Brick;
import dto.Paddle;
import ui.GameDrawable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BreakoutGameCanvas extends JComponent {

    private static final long serialVersionUID = -2271405512668523043L;

    /**
     * 当前局可绘制对象列表
     */
    private final List<GameDrawable> drawables;

    public BreakoutGameCanvas(Paddle paddle, Ball ball, List<Brick> bricks) {
        this.drawables = new ArrayList<>();
        this.drawables.add(paddle);
        this.drawables.add(ball);
        this.drawables.addAll(bricks);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        this.drawables.forEach(drawable -> drawable.draw(graphics));
    }

}
