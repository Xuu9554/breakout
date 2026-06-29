import lombok.Data;

import java.awt.*;
import java.io.Serializable;

@Data
public class Brick implements Serializable {

    private static final long serialVersionUID = -3703605258219341112L;

    public static final int HEIGHT = 16;

    private int width;

    private boolean alive = true;

    private int x;

    private int y;

    private Color color;

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (this.alive) {
            g2.setColor(this.color);
            g2.fillRect(this.x, this.y, this.width, Brick.HEIGHT);
        }
    }

}
