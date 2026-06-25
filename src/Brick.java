import java.awt.*;


public class Brick {
    private static String text=null;
    /**   Width of a brick   **/
    private int BRICK_WIDTH;
    /**   Height of a brick  **/
    public static final int BRICK_HEIGHT = 16;
    //砖块是否存在
    private boolean isAlive = true;
    //砖块坐标
    //private String text=null;
    private int x,y;
    private Color color=Color.white;
    private String colorful;

    public int getBRICK_WIDTH() {
        return BRICK_WIDTH;
    }

    public void setBRICK_WIDTH(int BRICK_WIDTH) {
        this.BRICK_WIDTH = BRICK_WIDTH;
    }

    public static int getBrickHeight() {
        return BRICK_HEIGHT;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void settext(String text){this.text=text;}

    public static String gettext(){ return text;}

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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
//    public void setColor(String colorful) {
//        this.colorful = colorful;
//    }

    //绘图
    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        if (isAlive){
            g2.setColor(color);
            g2.fillRect(x,y,BRICK_WIDTH,BRICK_HEIGHT);
        }
    }


}
