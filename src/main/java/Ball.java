import lombok.Data;

import java.awt.*;
import java.io.Serializable;

@Data
public class Ball implements Serializable {

    private static final long serialVersionUID = 6615576010331954937L;

    /**
     * 小球是否存活
     */
    private boolean alive = true;

    /**
     * 小球半径
     */
    private int radius = 10;

    /**
     * 小球左上角横坐标
     */
    private int x = 265;

    /**
     * 小球左上角纵坐标
     */
    private int y = 460;

    /**
     * 小球横向速度
     */
    private double velocityX = 3;

    /**
     * 小球纵向速度
     */
    private double velocityY = -2;

    /**
     * 小球剩余生命
     */
    private int life = 1;

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        g2.drawOval(this.x, this.y, this.radius, this.radius);
        g2.fillOval(this.x, this.y, this.radius, this.radius);
    }

    public void reverseVelocityX() {
        this.velocityX = -this.velocityX;
    }

    public void reverseVelocityY() {
        this.velocityY = -this.velocityY;
    }

    /**
     * 移动小球并处理墙面反弹
     *
     * @param boardWidth          游戏面板宽度
     * @param applicationHeight   游戏窗口高度
     * @param pause               是否暂停
     * @param remainingBrickCount 剩余砖块数
     * @param paddle              挡板
     * @return boolean 是否需要扣分
     */
    public boolean moveAndBounce(int boardWidth, int applicationHeight, boolean pause, int remainingBrickCount, Paddle paddle) {

        boolean shouldDeductScore = false;

        if (this.x < 0) {
            this.x = 0;
            this.reverseVelocityX();
        }

        if (this.x + 2 * this.radius > boardWidth && boardWidth != 0) {
            this.x = boardWidth - 2 * this.radius;
            this.reverseVelocityX();
        }

        if (this.y < 0) {
            this.y = 0;
            this.reverseVelocityY();
        }

        if (this.y + 2 * this.radius >= applicationHeight && boardWidth != 0) {
            this.reverseVelocityY();
            this.life--;
            if (this.life > 0) {
                shouldDeductScore = true;
            } else {
                this.alive = false;
            }
        }

        if (!pause) {
            if (remainingBrickCount >= BreakoutGameContext.BRICK_COUNT * 0.8 && remainingBrickCount <= BreakoutGameContext.BRICK_COUNT) {
                this.x += this.velocityX;
                this.y += this.velocityY;
            }
            if (remainingBrickCount >= BreakoutGameContext.BRICK_COUNT * 0.6 && remainingBrickCount < BreakoutGameContext.BRICK_COUNT * 0.8) {
                this.x += this.velocityX * 1.2;
                this.y += this.velocityY * 1.3;
                paddle.setSpeed(24);
            }
            if (remainingBrickCount >= BreakoutGameContext.BRICK_COUNT * 0.4 && remainingBrickCount < BreakoutGameContext.BRICK_COUNT * 0.6) {
                this.x += this.velocityX * 1.44;
                this.y += this.velocityY * 1.69;
                paddle.setSpeed(18);
            }
            if (remainingBrickCount >= BreakoutGameContext.BRICK_COUNT * 0.2 && remainingBrickCount < BreakoutGameContext.BRICK_COUNT * 0.4) {
                this.x += this.velocityX * 1.73;
                this.y += this.velocityY * 2.20;
                paddle.setSpeed(12);
            }
            if (remainingBrickCount < BreakoutGameContext.BRICK_COUNT * 0.2) {
                this.x += this.velocityX * 2.07;
                this.y += this.velocityY * 2.86;
                paddle.setSpeed(6);
            }
        }

        return shouldDeductScore;
    }

    public boolean collide(int objectX, int objectY, int objectWidth, int objectHeight) {
        return this.x + 2 * this.radius > objectX && this.x < objectX + objectWidth
                && this.y + 2 * this.radius > objectY && this.y < objectY + objectHeight;
    }

}
