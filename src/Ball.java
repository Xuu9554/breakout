import java.awt.*;

public class Ball {
    //游戏状态
    public boolean game_isAlive = true;
    //小球的半径
    public static int BALL_RADIUS = 10;
    //小球的初始位置
    private int x = 265;
    private int y = 460;
    //小球在x轴和y轴的初始速度
//    private double vx =2;
//    private double vy =-2;
    public static double vx = 3;
    public static double vy = -2;
    public static int life = 1;
    public static Color color = Color.red;

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        //绘制小球
        g2.setColor(color);
        g2.drawOval(x, y, BALL_RADIUS, BALL_RADIUS);
        g2.fillOval(x, y, BALL_RADIUS, BALL_RADIUS);

    }

    public static int getLife() {
        return life;
    }

    public static void setLife(int life) {
        Ball.life = life;
    }
    //    public static void setLife(int life) {
//        this.life = life;
//    }
//
//    public static int getLife() {
//        return life;
//    }

    public static void setBallRadius(int BALL_RADIUS) {
        Ball.BALL_RADIUS = BALL_RADIUS;
    }

    public static int getBallRadius() {
        return BALL_RADIUS;
    }

    public void rebounceX() {
        vx = -vx;
    }

    public void rebounceY() {
        vy = -vy;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public boolean isGame_isAlive() {
        return game_isAlive;
    }


    public void moveAndBounce(int success) {
        //左墙的反弹
        if (x < 0) {
            //当小球向左墙移动要移出边缘时，让小球回到边缘，并且反弹
            x = 0;
            rebounceX();
        }
        //右墙的反弹
        if (x + 2 * BALL_RADIUS > JBreakout.Width && JBreakout.Width != 0) {
            x = JBreakout.Width - 2 * BALL_RADIUS;
            rebounceX();
        }
        //上墙的反弹
        if (y < 0) {
            y = 0;
            rebounceY();
        }
        //下墙的反弹
        if (y + 2 * BALL_RADIUS >= JBreakout.APPLICATION_HEIGHT && JBreakout.Width != 0) {
            // game_isAlive = false;
            //final int i=life;
            rebounceY();
            life--;
            if (life > 0)
                JBreakout.score -= 50;//触底了，扣50
            else {
                game_isAlive = false;
                JBreakout.timer.cancel();

            }

        }
        //小球的移动
        if (!JBreakout.pause) {

            /*
             * 随着积分的上涨，小球的移动速度会加快，paddle的横移速度会变慢
             * */
            if (JBreakout.brickscount >= 80 && JBreakout.brickscount <= 100) {
                x += vx;
                y += vy;
            }
            if (JBreakout.brickscount >= 60 && JBreakout.brickscount < 80) {

                x += vx * 1.2;
                y += vy * 1.3;
                Paddle.speed = 24;
            }
            if (JBreakout.brickscount >= 40 && JBreakout.brickscount < 60) {

                x += vx * 1.44;
                y += vy * 1.69;
                Paddle.speed = 18;
            }
            if (JBreakout.brickscount >= 20 && JBreakout.brickscount < 40) {

                x += vx * 1.73;
                y += vy * 2.20;
                Paddle.speed = 12;
            }
            if (JBreakout.brickscount < 20) {

                x += vx * 2.07;
                y += vy * 2.86;
                Paddle.speed = 6;
            }
        }

    }


    public boolean collide(int object_x, int object_y, int object_width, int object_height) {
        //判断小球是否和其他物体碰撞
        if (this.x + 2 * BALL_RADIUS > object_x && this.x < object_x + object_width
                && this.y + 2 * BALL_RADIUS > object_y && this.y < object_y + object_height) {
            return true;
        }
        return false;


    }


    public void rebounce_paddle() {
        vy = -vy;
    }

    // 计算两点之间的距离
    public double DistanceBetweenTwoPoints(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    // 计算点(x, y)到经过两点(x1, y1)和(x2, y2)的直线的距离
    public double DistanceFromPointToLine(double x, double y, double x1, double y1, double x2, double y2) {
        double a = y2 - y1;
        double b = x1 - x2;
        double c = x2 * y1 - x1 * y2;

        assert (Math.abs(a) > 0.00001f || Math.abs(b) > 0.00001f);

        return Math.abs(a * x + b * y + c) / Math.sqrt(a * a + b * b);
    }

    public boolean IsCircleIntersectRectangle(double x, double y, double r, double x0, double y0, double x1, double y1, double x2, double y2) {
        double w1 = DistanceBetweenTwoPoints(x0, y0, x2, y2);
        double h1 = DistanceBetweenTwoPoints(x0, y0, x1, y1);
        double w2 = DistanceFromPointToLine(x, y, x0, y0, x1, y1);
        double h2 = DistanceFromPointToLine(x, y, x0, y0, x2, y2);

        if (w2 > w1 + r)
            return false;
        if (h2 > h1 + r)
            return false;

        if (w2 <= w1)
            return true;
        if (h2 <= h1)
            return true;

        return (w2 - w1) * (w2 - w1) + (h2 - h1) * (h2 - h1) <= r * r;
    }

}
