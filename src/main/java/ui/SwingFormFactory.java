package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Swing表单组件工厂
 */
@SuppressWarnings("UnusedReturnValue")
public class SwingFormFactory {

    private final JPanel panel;

    private final Font font;

    private SwingFormFactory(JPanel panel, Font font) {
        this.panel = panel;
        this.font = font;
    }

    /**
     * 创建绑定到面板的表单组件工厂
     *
     * @param panel 面板
     * @param font  字体
     * @return {@link SwingFormFactory} 表单组件工厂
     */
    public static SwingFormFactory with(JPanel panel, Font font) {
        return new SwingFormFactory(panel, font);
    }

    /**
     * 创建标签
     *
     * @param text   标签文本
     * @param x      横坐标
     * @param y      纵坐标
     * @param width  宽度
     * @param height 高度
     * @return {@link JLabel} 标签
     */
    public JLabel label(String text, int x, int y, int width, int height) {
        return this.add(new JLabel(text), x, y, width, height);
    }

    /**
     * 创建文本输入框
     *
     * @param x      横坐标
     * @param y      纵坐标
     * @param width  宽度
     * @param height 高度
     * @return {@link JTextField} 文本输入框
     */
    public JTextField textField(int x, int y, int width, int height) {
        return this.add(new JTextField(), x, y, width, height);
    }

    /**
     * 创建密码输入框
     *
     * @param x      横坐标
     * @param y      纵坐标
     * @param width  宽度
     * @param height 高度
     * @return {@link JPasswordField} 密码输入框
     */
    public JPasswordField passwordField(int x, int y, int width, int height) {
        JPasswordField passwordField = this.add(new JPasswordField(), x, y, width, height);
        passwordField.setEchoChar('*');
        return passwordField;
    }

    /**
     * 添加按钮
     *
     * @param button     按钮
     * @param x          横坐标
     * @param y          纵坐标
     * @param width      宽度
     * @param height     高度
     * @param background 背景色
     * @param <T>        按钮类型
     * @return {@link T} 按钮
     */
    public <T extends AbstractButton> T button(T button, int x, int y, int width, int height, Color background) {
        button.setBackground(background);
        return this.add(button, x, y, width, height);
    }

    private <T extends JComponent> T add(T component, int x, int y, int width, int height) {
        component.setBounds(x, y, width, height);
        component.setFont(this.font);
        this.panel.add(component);
        return component;
    }

}
