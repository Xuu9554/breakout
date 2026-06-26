package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Swing表单组件工厂
 */
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
        return this.component(new JLabel(text), x, y, width, height);
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
        return this.component(new JTextField(), x, y, width, height);
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
        JPasswordField passwordField = this.component(new JPasswordField(), x, y, width, height);
        passwordField.setEchoChar('*');
        return passwordField;
    }

    /**
     * 创建普通按钮并加入面板
     *
     * @param buttonName 按钮文案
     * @param x          横坐标
     * @param y          纵坐标
     * @param width      宽度
     * @param height     高度
     * @param background 背景色
     * @return {@link JButton} 按钮
     */
    public JButton button(String buttonName, int x, int y, int width, int height, Color background) {
        return this.button(new JButton(buttonName), x, y, width, height, background);
    }

    /**
     * 添加自定义按钮并统一设置表单样式
     *
     * @param button     按钮组件
     * @param x          横坐标
     * @param y          纵坐标
     * @param width      宽度
     * @param height     高度
     * @param background 背景色
     * @param <T>        按钮类型
     * @return T
     */
    public <T extends AbstractButton> T button(T button, int x, int y, int width, int height, Color background) {
        button.setBackground(background);
        return this.component(button, x, y, width, height);
    }

    /**
     * 添加组件
     *
     * @param component 组件
     * @param x         横坐标
     * @param y         纵坐标
     * @param width     宽度
     * @param height    高度
     * @param <T>       组件类型
     * @return {@link T} 组件
     */
    public <T extends JComponent> T component(T component, int x, int y, int width, int height) {
        component.setBounds(x, y, width, height);
        component.setFont(this.font);
        this.panel.add(component);
        return component;
    }

}
