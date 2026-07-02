package dto;

import lombok.Data;
import support.BreakoutGameContext;
import ui.GameDrawable;

import java.awt.*;
import java.io.Serializable;

@Data
public class Brick implements Serializable, GameDrawable {

    private static final long serialVersionUID = -3703605258219341112L;

    /**
     * 砖块宽度
     */
    private int width;

    /**
     * 砖块是否存活
     */
    private boolean alive = true;

    /**
     * 砖块剩余生命
     */
    private int life = 1;

    /**
     * 砖块左上角横坐标
     */
    private int x;

    /**
     * 砖块左上角纵坐标
     */
    private int y;

    /**
     * 砖块颜色
     */
    private Color color;

    /**
     * 处理一次砖块命中
     *
     * @return boolean 本次命中后砖块是否被消除
     */
    public boolean hit() {

        if (!this.alive) {
            return false;
        }

        this.life--;
        if (this.life <= 0) {
            this.alive = false;
            return true;
        }

        return false;
    }

    @Override
    public void draw(Graphics2D graphics) {

        if (!this.alive) {
            return;
        }

        graphics.setColor(this.color);
        graphics.fillRect(this.x, this.y, this.width, HEIGHT);

        if (!BreakoutGameContext.SHOW_LIFE) {
            return;
        }

        Color oldColor = graphics.getColor();
        Font oldFont = graphics.getFont();

        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font(oldFont.getName(), Font.BOLD, 14));

        String lifeText = String.valueOf(this.life);
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int textX = this.x + (this.width - fontMetrics.stringWidth(lifeText)) / 2;
        int textY = this.y + (HEIGHT - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();
        graphics.drawString(lifeText, textX, textY);

        graphics.setColor(oldColor);
        graphics.setFont(oldFont);
    }

    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 砖块高度
     */
    public final static int HEIGHT = 16;

}
