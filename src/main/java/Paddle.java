import lombok.Data;

import java.awt.*;
import java.io.Serializable;

@Data
public class Paddle implements Serializable {

    private static final long serialVersionUID = 7510495643437996449L;

    /**
     * 挡板移动速度
     */
    private int speed = 30;

    /**
     * 挡板左上角横坐标
     */
    private int x = 0;

    /**
     * 挡板左上角纵坐标
     */
    private int y = 0;

    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 挡板初始位置距离面板底部的偏移量
     */
    private static final int PADDLE_Y_OFFSET = 180;

    /**
     * 挡板绘制宽度
     */
    private static final int DRAW_PADDLE_WIDTH = 90;

    /**
     * 挡板绘制高度
     */
    private static final int DRAW_PADDLE_HEIGHT = 20;

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawRect(this.x, this.y, DRAW_PADDLE_WIDTH, DRAW_PADDLE_HEIGHT);
        g2.setColor(Color.BLACK);
        g2.fillRect(this.x, this.y, DRAW_PADDLE_WIDTH, DRAW_PADDLE_HEIGHT);
    }

    /**
     * 根据游戏面板尺寸设置挡板初始位置
     *
     * @param boardWidth  游戏面板宽度
     * @param boardHeight 游戏面板高度
     */
    public void setStartPosition(int boardWidth, int boardHeight) {
        this.x = (boardWidth - DRAW_PADDLE_WIDTH) / 2;
        this.y = boardHeight - PADDLE_Y_OFFSET;
    }

    public int getPaddleWidth() {
        return DRAW_PADDLE_WIDTH;
    }

    public int getPaddleHeight() {
        return DRAW_PADDLE_HEIGHT;
    }

    /**
     * 按当前游戏状态向右移动挡板
     *
     * @param boardWidth 游戏面板宽度
     * @param pause      是否暂停
     */
    public void moveRight(int boardWidth, boolean pause) {
        if (!pause) {
            if ((this.x + this.speed) <= boardWidth - DRAW_PADDLE_WIDTH) {
                this.x += this.speed;
            } else {
                this.x = boardWidth - DRAW_PADDLE_WIDTH;
            }
        }
    }

    /**
     * 按当前游戏状态向左移动挡板
     *
     * @param pause 是否暂停
     */
    public void moveLeft(boolean pause) {
        if (!pause) {
            if (this.x >= this.speed) {
                this.x -= this.speed;
            } else {
                this.x = 0;
            }
        }
    }

}
