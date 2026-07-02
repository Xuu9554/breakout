package dto;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import support.BreakoutGameContext;
import ui.GameDrawable;

import java.awt.*;
import java.awt.geom.Ellipse2D;
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

    /**
     * 小球初始位置距离挡板的间距
     */
    private final static int START_PADDLE_GAP = 0;

    /**
     * 小球最小横向速度倍率
     */
    private final static double MIN_HORIZONTAL_SPEED_RATE = 1.0;

    /**
     * 小球最大横向速度倍率
     */
    private final static double MAX_HORIZONTAL_SPEED_RATE = 3.1;

    /**
     * 小球最小纵向速度倍率
     */
    private final static double MIN_VERTICAL_SPEED_RATE = 1.0;

    /**
     * 小球最大纵向速度倍率
     */
    private final static double MAX_VERTICAL_SPEED_RATE = 2.2;

    /**
     * 小球难度增长曲线指数
     */
    private final static double DIFFICULTY_CURVE_POWER = 1.25;

    /**
     * 小球最小基础速度
     */
    private final static double MIN_BASE_SPEED = 1.2;

    /**
     * 小球最小轴向速度分量
     */
    private final static double MIN_AXIS_SPEED = 1.0;

    /**
     * 小球生命字号
     */
    private final static int LIFE_FONT_SIZE = 10;

    /**
     * 小球生命文本颜色
     */
    private final static Color LIFE_TEXT_COLOR = Color.WHITE;

    @Override
    public void draw(Graphics2D graphics) {

        if (!this.alive) {
            return;
        }

        Graphics2D ballGraphics = (Graphics2D) graphics.create();
        ballGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ballGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        ballGraphics.setColor(Color.RED);
        ballGraphics.fill(new Ellipse2D.Double(this.x, this.y, this.radius, this.radius));

        if (!BreakoutGameContext.SHOW_LIFE) {
            ballGraphics.dispose();
            return;
        }

        Font oldFont = ballGraphics.getFont();

        ballGraphics.setColor(Ball.LIFE_TEXT_COLOR);
        ballGraphics.setFont(new Font(oldFont.getName(), Font.BOLD, Ball.LIFE_FONT_SIZE));

        String lifeText = String.valueOf(this.life);
        FontMetrics fontMetrics = ballGraphics.getFontMetrics();
        int textX = this.x + (this.radius - fontMetrics.stringWidth(lifeText)) / 2;
        int textY = this.y + (this.radius - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();
        ballGraphics.drawString(lifeText, textX, textY);
        ballGraphics.dispose();
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
     * 根据挡板位置设置小球初始位置
     *
     * @param paddle 挡板
     */
    public void setStartPosition(Paddle paddle) {
        this.x = paddle.getX() + (paddle.getPaddleWidth() - this.radius) / 2;
        this.y = paddle.getY() - this.radius - START_PADDLE_GAP;
        this.previousX = this.x;
        this.previousY = this.y;
    }

    /**
     * 移动小球并处理墙面反弹
     *
     * @param boardWidth          游戏面板宽度
     * @param applicationHeight   游戏窗口高度
     * @param pause               是否暂停
     * @param remainingBrickCount 剩余砖块数
     * @param initialBrickCount   初始砖块数
     * @return boolean 是否需要扣分
     */
    public boolean moveAndBounce(int boardWidth, int applicationHeight, boolean pause, int remainingBrickCount, int initialBrickCount) {

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

            double clearedProgress = Math.max(0, Math.min(1, 1 - (double) remainingBrickCount / initialBrickCount));
            double difficultyProgress = Math.pow(clearedProgress, DIFFICULTY_CURVE_POWER);
            double horizontalSpeedRate = MIN_HORIZONTAL_SPEED_RATE + (MAX_HORIZONTAL_SPEED_RATE - MIN_HORIZONTAL_SPEED_RATE) * difficultyProgress;
            double verticalSpeedRate = MIN_VERTICAL_SPEED_RATE + (MAX_VERTICAL_SPEED_RATE - MIN_VERTICAL_SPEED_RATE) * difficultyProgress;

            this.x += this.calculateFrameMove(this.velocityX * horizontalSpeedRate);
            this.y += this.calculateFrameMove(this.velocityY * verticalSpeedRate);
        }

        return shouldDeductScore;
    }

    /**
     * 计算当前帧实际位移
     *
     * @param velocity 当前轴向速度
     * @return int 当前帧实际位移
     */
    private int calculateFrameMove(double velocity) {
        int frameMove = (int) Math.round(velocity);
        if (frameMove == 0 && velocity != 0) {
            return velocity > 0 ? 1 : -1;
        }
        return frameMove;
    }

    /**
     * 判断小球是否与矩形发生碰撞
     *
     * @param objectX      矩形左上角横坐标
     * @param objectY      矩形左上角纵坐标
     * @param objectWidth  矩形宽度
     * @param objectHeight 矩形高度
     * @return boolean 是否发生碰撞
     */
    public boolean collide(int objectX, int objectY, int objectWidth, int objectHeight) {

        double closestX = Math.max(objectX, Math.min(this.centerX(), objectX + objectWidth));
        double closestY = Math.max(objectY, Math.min(this.centerY(), objectY + objectHeight));
        double distanceX = this.centerX() - closestX;
        double distanceY = this.centerY() - closestY;
        double collisionRadius = this.radius / 2.0;

        return distanceX * distanceX + distanceY * distanceY <= collisionRadius * collisionRadius;
    }

    /**
     * 判断是否与另一个小球发生碰撞
     *
     * @param otherBall 另一个小球
     * @return boolean 是否发生碰撞
     */
    public boolean collide(Ball otherBall) {
        double distanceX = otherBall.centerX() - this.centerX();
        double distanceY = otherBall.centerY() - this.centerY();
        double minDistance = (this.radius + otherBall.radius) / 2.0;
        return distanceX * distanceX + distanceY * distanceY <= minDistance * minDistance;
    }

    /**
     * 和另一个小球进行弹性碰撞
     *
     * @param otherBall 另一个小球
     */
    public void bounceOffBall(Ball otherBall) {

        if (!this.collide(otherBall)) {
            return;
        }

        double distanceX = otherBall.centerX() - this.centerX();
        double distanceY = otherBall.centerY() - this.centerY();
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        if (distance == 0) {
            distanceX = 1;
            distanceY = 0;
            distance = 1;
        }

        double normalX = distanceX / distance;
        double normalY = distanceY / distance;
        this.separateFromBall(otherBall, normalX, normalY, distance);

        double velocityDifferenceX = this.velocityX - otherBall.velocityX;
        double velocityDifferenceY = this.velocityY - otherBall.velocityY;
        double velocityAlongNormal = velocityDifferenceX * normalX + velocityDifferenceY * normalY;

        if (velocityAlongNormal <= 0) {
            return;
        }

        this.velocityX -= velocityAlongNormal * normalX;
        this.velocityY -= velocityAlongNormal * normalY;
        otherBall.velocityX += velocityAlongNormal * normalX;
        otherBall.velocityY += velocityAlongNormal * normalY;

        this.keepMinimumVelocity(-normalX, -normalY);
        otherBall.keepMinimumVelocity(normalX, normalY);
    }

    /**
     * 将两个重叠小球沿碰撞法线分开
     *
     * @param otherBall 另一个小球
     * @param normalX   碰撞法线X轴分量
     * @param normalY   碰撞法线Y轴分量
     * @param distance  两个小球圆心距离
     */
    private void separateFromBall(Ball otherBall, double normalX, double normalY, double distance) {

        double minDistance = (this.radius + otherBall.radius) / 2.0;
        double overlap = minDistance - distance;

        if (overlap <= 0) {
            return;
        }

        this.x = (int) Math.round(this.x - normalX * overlap / 2);
        this.y = (int) Math.round(this.y - normalY * overlap / 2);
        otherBall.x = (int) Math.round(otherBall.x + normalX * overlap / 2);
        otherBall.y = (int) Math.round(otherBall.y + normalY * overlap / 2);
    }

    /**
     * 保持小球最低基础速度
     *
     * @param directionX 补偿方向X轴分量
     * @param directionY 补偿方向Y轴分量
     */
    private void keepMinimumVelocity(double directionX, double directionY) {

        if (Math.sqrt(this.velocityX * this.velocityX + this.velocityY * this.velocityY) >= MIN_BASE_SPEED) {
            return;
        }

        double directionLength = Math.sqrt(directionX * directionX + directionY * directionY);
        if (directionLength == 0) {
            directionX = this.velocityX == 0 ? 1 : this.velocityX;
            directionY = this.velocityY == 0 ? -1 : this.velocityY;
            directionLength = Math.sqrt(directionX * directionX + directionY * directionY);
        }

        this.velocityX = directionX / directionLength * MIN_BASE_SPEED;
        this.velocityY = directionY / directionLength * MIN_BASE_SPEED;

        // 保持小球最低轴向速度分量
        if (this.velocityX != 0 && Math.abs(this.velocityX) < MIN_AXIS_SPEED) {
            this.velocityX = this.velocityX > 0 ? MIN_AXIS_SPEED : -MIN_AXIS_SPEED;
        }

        if (this.velocityY != 0 && Math.abs(this.velocityY) < MIN_AXIS_SPEED) {
            this.velocityY = this.velocityY > 0 ? MIN_AXIS_SPEED : -MIN_AXIS_SPEED;
        }
    }

    /**
     * 获取小球圆心横坐标
     *
     * @return double 小球圆心横坐标
     */
    private double centerX() {
        return this.x + this.radius / 2.0;
    }

    /**
     * 获取小球圆心纵坐标
     *
     * @return double 小球圆心纵坐标
     */
    private double centerY() {
        return this.y + this.radius / 2.0;
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

            if (ObjectUtil.isNull(hitBrick) || currentHitTime < earliestHitTime) {
                // 保留当前帧中最早发生碰撞的砖块
                hitBrick = brick;
                earliestHitTime = currentHitTime;
            }
        }

        return hitBrick;
    }

}
