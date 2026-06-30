import lombok.Data;
import ui.GameDrawable;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

@Data
public class Ball implements Serializable, GameDrawable {

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
     * 小球上一帧左上角横坐标
     */
    private int previousX = 265;

    /**
     * 小球上一帧左上角纵坐标
     */
    private int previousY = 460;

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

    @Override
    public void draw(Graphics2D graphics) {
        graphics.setColor(Color.RED);
        graphics.drawOval(this.x, this.y, this.radius, this.radius);
        graphics.fillOval(this.x, this.y, this.radius, this.radius);
    }

    /**
     * 反转X轴运动方向
     */
    public void reverseVelocityX() {
        this.velocityX = -this.velocityX;
    }

    /**
     * 反转Y轴运动方向
     */
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

        if (this.x + this.radius > boardWidth && boardWidth != 0) {
            this.x = boardWidth - this.radius;
            this.reverseVelocityX();
        }

        if (this.y < 0) {
            this.y = 0;
            this.reverseVelocityY();
        }

        if (this.y + this.radius >= applicationHeight && boardWidth != 0) {
            this.reverseVelocityY();
            this.life--;
            if (this.life > 0) {
                shouldDeductScore = true;
            } else {
                this.alive = false;
            }
        }

        if (!pause) {

            this.previousX = this.x;
            this.previousY = this.y;

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
        return this.x + this.radius > objectX && this.x < objectX + objectWidth
                && this.y + this.radius > objectY && this.y < objectY + objectHeight;
    }

    /**
     * 根据碰撞面反弹并将小球移出矩形区域
     *
     * @param objectX      矩形左上角横坐标
     * @param objectY      矩形左上角纵坐标
     * @param objectWidth  矩形宽度
     * @param objectHeight 矩形高度
     */
    public void bounceOffRectangle(int objectX, int objectY, int objectWidth, int objectHeight) {

        int objectRight = objectX + objectWidth;
        int objectBottom = objectY + objectHeight;

        // 小球上一帧的右边界和下边界，用于判断小球是从哪个方向撞入矩形的
        int previousRight = this.previousX + this.radius;
        int previousBottom = this.previousY + this.radius;

        if (previousBottom <= objectY && this.velocityY > 0) {
            // 上一帧小球在矩形上方，并且当前正在向下运动，说明撞到了矩形顶部
            this.y = objectY - this.radius;
            this.reverseVelocityY();
            return;
        }

        if (this.previousY >= objectBottom && this.velocityY < 0) {
            // 上一帧小球在矩形下方，并且当前正在向上运动，说明撞到了矩形底部
            this.y = objectBottom;
            this.reverseVelocityY();
            return;
        }

        if (previousRight <= objectX && this.velocityX > 0) {
            // 上一帧小球在矩形左侧，并且当前正在向右运动，说明撞到了矩形左侧
            this.x = objectX - this.radius;
            this.reverseVelocityX();
            return;
        }

        if (this.previousX >= objectRight && this.velocityX < 0) {
            // 上一帧小球在矩形右侧，并且当前正在向左运动，说明撞到了矩形右侧
            this.x = objectRight;
            this.reverseVelocityX();
            return;
        }

        int ballRight = this.x + this.radius;
        int ballBottom = this.y + this.radius;

        // 当上一帧位置无法明确判断碰撞面时，根据当前重叠深度判断
        int leftOverlap = ballRight - objectX;
        int rightOverlap = objectRight - this.x;
        int topOverlap = ballBottom - objectY;
        int bottomOverlap = objectBottom - this.y;

        int horizontalOverlap = Math.min(leftOverlap, rightOverlap);
        int verticalOverlap = Math.min(topOverlap, bottomOverlap);

        if (horizontalOverlap < verticalOverlap) {
            // 横向重叠更浅，说明更像是从左侧或右侧撞入，反转X轴速度
            this.x = leftOverlap < rightOverlap ? objectX - this.radius : objectRight;
            this.reverseVelocityX();
        } else {
            // 纵向重叠更浅，说明更像是从上方或下方撞入，反转Y轴速度
            this.y = topOverlap < bottomOverlap ? objectY - this.radius : objectBottom;
            this.reverseVelocityY();
        }
    }

    /**
     * 查找小球当前帧最先撞到的砖块
     *
     * @param bricks 砖块列表
     * @return {@link Brick} 被撞到的砖块
     */
    public Brick findFirstHitBrick(List<Brick> bricks) {

        Brick hitBrick = null;
        double earliestHitTime = Double.POSITIVE_INFINITY;

        for (Brick brick : bricks) {

            if (!brick.isAlive() || !this.collide(brick.getX(), brick.getY(), brick.getWidth(), Brick.HEIGHT)) {
                // 只处理存活且当前帧已经与小球发生重叠的砖块
                continue;
            }

            // 当前帧小球在X轴和Y轴上的实际位移
            double frameMoveX = this.x - this.previousX;
            double frameMoveY = this.y - this.previousY;

            double horizontalHitTime = Double.NEGATIVE_INFINITY;
            double verticalHitTime = Double.NEGATIVE_INFINITY;

            if (frameMoveX > 0) {
                // 小球向右移动时，计算小球右边界撞到砖块左边界的时间比例
                horizontalHitTime = (double) (brick.getX() - this.previousX - this.radius) / frameMoveX;
            } else if (frameMoveX < 0) {
                // 小球向左移动时，计算小球左边界撞到砖块右边界的时间比例
                horizontalHitTime = (double) (brick.getX() + brick.getWidth() - this.previousX) / frameMoveX;
            }

            if (frameMoveY > 0) {
                // 小球向下移动时，计算小球下边界撞到砖块上边界的时间比例
                verticalHitTime = (double) (brick.getY() - this.previousY - this.radius) / frameMoveY;
            } else if (frameMoveY < 0) {
                // 小球向上移动时，计算小球上边界撞到砖块下边界的时间比例
                verticalHitTime = (double) (brick.getY() + Brick.HEIGHT - this.previousY) / frameMoveY;
            }

            // 矩形碰撞需要X轴和Y轴都进入重叠区，因此较晚进入的那个时间才是真正发生碰撞的时间
            double currentHitTime = Math.max(horizontalHitTime, verticalHitTime);

            if (hitBrick == null || currentHitTime < earliestHitTime) {
                // 保留当前帧中最早发生碰撞的砖块
                hitBrick = brick;
                earliestHitTime = currentHitTime;
            }
        }

        return hitBrick;
    }

}
