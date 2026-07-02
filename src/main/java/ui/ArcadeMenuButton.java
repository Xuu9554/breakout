package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ArcadeMenuButton extends JButton {

    private static final long serialVersionUID = 7419678233621051135L;

    /**
     * 街机风菜单按钮
     */
    public ArcadeMenuButton(String text) {
        super(text);
        this.setContentAreaFilled(false);
        this.setOpaque(false);
        this.setRolloverEnabled(true);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.setVerticalTextPosition(SwingConstants.CENTER);
        this.setMargin(new Insets(0, 0, 0, 0));
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 8));
    }

    /**
     * 绘制按钮外观
     *
     * @param graphics 图形上下文
     */
    @Override
    protected void paintComponent(Graphics graphics) {

        Graphics2D canvas = (Graphics2D) graphics.create();
        canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color background = this.getBackground();
        if (this.getModel().isPressed()) {
            background = background.darker();
        } else if (this.getModel().isRollover()) {
            background = background.brighter();
        }

        canvas.setColor(new Color(0, 0, 0, 70));
        canvas.fill(new RoundRectangle2D.Double(4, 6, this.getWidth() - 8, this.getHeight() - 8, 22, 22));

        canvas.setColor(background);
        canvas.fill(new RoundRectangle2D.Double(0, 0, this.getWidth() - 8, this.getHeight() - 8, 22, 22));

        canvas.setColor(new Color(255, 255, 255, 150));
        canvas.setStroke(new BasicStroke(2F));
        canvas.draw(new RoundRectangle2D.Double(1, 1, this.getWidth() - 10, this.getHeight() - 10, 22, 22));
        canvas.dispose();
        super.paintComponent(graphics);
    }

}
