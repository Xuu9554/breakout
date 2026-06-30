package ui;

import java.awt.*;

@FunctionalInterface
public interface GameDrawable {

    /**
     * 绘制游戏对象
     *
     * @param graphics 图形上下文
     */
    void draw(Graphics2D graphics);

}
