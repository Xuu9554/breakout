package dto;

import lombok.Data;
import ui.GameDrawable;

import java.awt.*;
import java.io.Serializable;

@Data
public class Brick implements Serializable, GameDrawable {

    private static final long serialVersionUID = -3703605258219341112L;

    /**
     * 砖块高度
     */
    public final static int HEIGHT = 16;

    /**
     * 砖块宽度
     */
    private int width;

    /**
     * 砖块是否存活
     */
    private boolean alive = true;

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

    @Override
    public void draw(Graphics2D graphics) {
        if (this.alive) {
            graphics.setColor(this.color);
            graphics.fillRect(this.x, this.y, this.width, Brick.HEIGHT);
        }
    }

}
