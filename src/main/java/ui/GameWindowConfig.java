package ui;

import lombok.Data;
import lombok.experimental.Accessors;

import java.awt.*;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class GameWindowConfig implements Serializable {

    private static final long serialVersionUID = 5875850158693566167L;

    /**
     * 窗口标题
     */
    private final String title;

    /**
     * 窗口左上角横坐标
     */
    private final int x;

    /**
     * 窗口左上角纵坐标
     */
    private final int y;

    /**
     * 窗口宽度
     */
    private final int width;

    /**
     * 窗口高度
     */
    private final int height;

    /**
     * 是否允许调整窗口大小
     */
    private boolean resizable = false;

    /**
     * 窗口关闭行为
     */
    private Integer closeOperation;

    /**
     * 根面板背景色
     */
    private Color background = Color.WHITE;

    /**
     * 根面板布局管理器
     */
    private LayoutManager layout;

    private GameWindowConfig(String title, int x, int y, int width, int height) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * 创建固定大小和位置的窗口配置
     *
     * @param title  窗口标题
     * @param x      横坐标
     * @param y      纵坐标
     * @param width  窗口宽度
     * @param height 窗口高度
     * @return {@link GameWindowConfig} 窗口配置
     */
    public static GameWindowConfig of(String title, int x, int y, int width, int height) {
        return new GameWindowConfig(title, x, y, width, height);
    }

}
