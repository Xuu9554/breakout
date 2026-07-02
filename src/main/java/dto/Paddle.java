package dto;

import lombok.Data;
import ui.GameDrawable;

import java.awt.*;
import java.io.Serializable;

@Data
public class Paddle implements Serializable, GameDrawable {

    private static final long serialVersionUID = 7510495643437996449L;

    /**
     * 挡板移动速度
     */
    private int speed = MIN_PADDLE_SPEED;

    /**
     * 挡板当前宽度
     */
    private int width = MIN_PADDLE_WIDTH;

    /**
     * 挡板左上角横坐标
     */
    private int x = 0;

    /**
     * 挡板左上角纵坐标
     */
    private int y = 0;

    // ------------------------------------------------------------------------------------------------------------------------

    @Override
    public void draw(Graphics2D graphics) {
        Object oldAntialias = graphics.getRenderingHint(RenderingHints.KEY_ANTIALIASING);

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(new Color(236, 239, 241));
        graphics.fillRoundRect(this.x, this.y, this.width, DRAW_PADDLE_HEIGHT, 8, 8);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialias);
    }

    /**
     * 根据游戏面板尺寸设置挡板初始位置
     *
     * @param boardWidth  游戏面板宽度
     * @param boardHeight 游戏面板高度
     */
    public void setStartPosition(int boardWidth, int boardHeight) {
        this.x = (boardWidth - this.width) / 2;
        this.y = boardHeight - PADDLE_Y_OFFSET;
    }

    /**
     * 按清砖进度调整挡板能力
     *
     * @param remainingBrickCount 剩余砖块数
     * @param initialBrickCount   初始砖块数
     * @param boardWidth          游戏面板宽度
     */
    public void adjustByClearedBrickProgress(int remainingBrickCount, int initialBrickCount, int boardWidth) {

        int oldWidth = this.width;
        double clearedProgress = Math.max(0, Math.min(1, 1 - (double) remainingBrickCount / initialBrickCount));

        this.width = MIN_PADDLE_WIDTH + (int) Math.round((MAX_PADDLE_WIDTH - MIN_PADDLE_WIDTH) * clearedProgress);
        this.speed = MIN_PADDLE_SPEED + (int) Math.round((MAX_PADDLE_SPEED - MIN_PADDLE_SPEED) * clearedProgress);

        int centerX = this.x + oldWidth / 2;
        this.x = Math.max(0, Math.min(centerX - this.width / 2, boardWidth - this.width));
    }

    /**
     * 获取挡板宽度
     *
     * @return int 挡板宽度
     */
    public int getPaddleWidth() {
        return this.width;
    }

    /**
     * 获取挡板高度
     *
     * @return int 挡板高度
     */
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
            if ((this.x + this.speed) <= boardWidth - this.width) {
                this.x += this.speed;
            } else {
                this.x = boardWidth - this.width;
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

    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 挡板初始位置距离面板底部的偏移量
     */
    private final static int PADDLE_Y_OFFSET = 30;

    /**
     * 挡板最小移动速度
     */
    private final static int MIN_PADDLE_SPEED = 30;

    /**
     * 挡板最大移动速度
     */
    private final static int MAX_PADDLE_SPEED = 52;

    /**
     * 挡板最小绘制宽度
     */
    private final static int MIN_PADDLE_WIDTH = 134;

    /**
     * 挡板最大绘制宽度
     */
    private final static int MAX_PADDLE_WIDTH = 190;

    /**
     * 挡板绘制高度
     */
    private final static int DRAW_PADDLE_HEIGHT = 4;

}
