package ui;

import javax.swing.*;
import java.awt.*;

public class SwingDialogs {

    /**
     * 展示普通提示
     *
     * @param parent  父组件
     * @param message 提示内容
     */
    public static void information(Component parent, String message) {
        information(parent, message, "提示");
    }

    /**
     * 展示普通提示
     *
     * @param parent  父组件
     * @param message 提示内容
     * @param title   提示标题
     */
    public static void information(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 展示警告提示
     *
     * @param parent  父组件
     * @param message 提示内容
     */
    public static void warning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "提示", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * 展示错误提示
     *
     * @param parent  父组件
     * @param message 提示内容
     */
    public static void error(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * 展示确认提示
     *
     * @param parent  父组件
     * @param message 提示内容
     * @return boolean 是否确认
     */
    public static boolean confirm(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "提示", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

}
