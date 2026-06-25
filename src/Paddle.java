import java.awt.*;

public class Paddle {
    //public static final int PADDLE_WIDTH=60;
    //public static final int PADDLE_HEIGHT=10;
    private static final int PADDLE_Y_OFFSET = 180;
    public static final int drawPADDLE_WIDTH = 90;
    public static final int drawPADDLE_HEIGHT = 20;
    private int x = 0;
    private int y = 0;
    public static int speed = 30;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static int getSpeed() {
        return speed;
    }

    public static void setSpeed(int speed) {
        Paddle.speed = speed;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawRect(x, y, drawPADDLE_WIDTH, drawPADDLE_HEIGHT);
        g2.setColor(Color.BLACK);
        g2.fillRect(x, y, drawPADDLE_WIDTH, drawPADDLE_HEIGHT);
    }

    public void setStartPosition() {
        x = (JBreakout.Width - drawPADDLE_WIDTH) / 2;
        y = JBreakout.Height - PADDLE_Y_OFFSET;
    }

    public int getPaddleWidth() {
        return drawPADDLE_WIDTH;
    }

    public int getPaddleHeight() {
        return drawPADDLE_HEIGHT;
    }

    public void moveRight() {
        if (JBreakout.pause == false) {
            if ((x + speed) <= JBreakout.Width - drawPADDLE_WIDTH)
                x += speed;
            else
                x = JBreakout.Width - drawPADDLE_WIDTH;
        }
    }

    public void moveLeft() {
        if (JBreakout.pause == false) {
            if (x >= speed)
                x -= speed;
            else
                x = 0;
        }

    }


}
