package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class MainMenuPanel extends JPanel {

    private static final long serialVersionUID = -4869405699167138748L;

    public MainMenuPanel(LayoutManager layout) {
        super(layout);
        this.setOpaque(false);
    }

    /**
     * 绘制主菜单背景
     *
     * @param graphics 图形上下文
     */
    @Override
    protected void paintComponent(Graphics graphics) {

        Graphics2D canvas = (Graphics2D) graphics.create();
        canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        canvas.setPaint(new GradientPaint(0, 0, BACKGROUND_START_COLOR, 0, this.getHeight(), BACKGROUND_END_COLOR));
        canvas.fillRect(0, 0, this.getWidth(), this.getHeight());

        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 7; column++) {
                canvas.setColor(BRICK_DECORATION_COLORS[(row + column) % BRICK_DECORATION_COLORS.length]);
                int x = 52 + column * 64 + (row % 2) * 18;
                int y = 24 + row * 22;
                canvas.fill(new RoundRectangle2D.Double(x, y, 44, 10, 6, 6));
            }
        }

        canvas.setStroke(new BasicStroke(3.5F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        canvas.setColor(new Color(255, 255, 255, 48));
        canvas.drawLine(88, 620, 212, 190);
        canvas.drawLine(438, 610, 360, 220);

        canvas.setColor(new Color(255, 82, 82));
        canvas.fill(new Ellipse2D.Double(78, 610, 16, 16));
        canvas.setColor(new Color(64, 196, 255));
        canvas.fill(new Ellipse2D.Double(354, 212, 14, 14));

        canvas.dispose();
        super.paintComponent(graphics);
    }

    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 背景渐变开始色
     */
    private final static Color BACKGROUND_START_COLOR = new Color(11, 20, 38);

    /**
     * 背景渐变结束色
     */
    private final static Color BACKGROUND_END_COLOR = new Color(31, 12, 55);

    /**
     * 砖块装饰颜色
     */
    private final static Color[] BRICK_DECORATION_COLORS = {
            new Color(211, 47, 47, 160),
            new Color(230, 81, 0, 150),
            new Color(156, 111, 0, 150),
            new Color(46, 125, 50, 150),
            new Color(0, 131, 143, 150),
            new Color(25, 118, 210, 150),
            new Color(123, 31, 162, 150)
    };

}
