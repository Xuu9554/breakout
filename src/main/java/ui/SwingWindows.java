package ui;

import cn.hutool.core.lang.Opt;

import javax.swing.*;
import java.awt.*;

public class SwingWindows {

    /**
     * 隐藏当前组件所在窗口
     *
     * @param component 当前组件
     */
    public static void hide(Component component) {
        Opt.ofNullable(findWindow(component)).ifPresent(window -> window.setVisible(false));
    }

    /**
     * 释放当前组件所在窗口
     *
     * @param component 当前组件
     */
    public static void dispose(Component component) {
        Opt.ofNullable(findWindow(component)).ifPresent(window -> {
            window.setVisible(false);
            window.dispose();
        });
    }

    /**
     * 隐藏当前窗口后打开新窗口
     *
     * @param component    当前组件
     * @param windowAction 新窗口打开动作
     */
    public static void hideAndOpen(Component component, Runnable windowAction) {
        hide(component);
        windowAction.run();
    }

    /**
     * 释放当前窗口后打开新窗口
     *
     * @param component    当前组件
     * @param windowAction 新窗口打开动作
     */
    public static void disposeAndOpen(Component component, Runnable windowAction) {
        dispose(component);
        windowAction.run();
    }

    /**
     * 查找当前组件所在窗口
     *
     * @param component 当前组件
     * @return {@link Window} 当前组件所在窗口
     */
    private static Window findWindow(Component component) {
        if (component instanceof Window) {
            return (Window) component;
        }
        return SwingUtilities.getWindowAncestor(component);
    }

}
