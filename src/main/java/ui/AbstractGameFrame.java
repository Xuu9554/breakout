package ui;

import cn.hutool.core.lang.Opt;

import javax.swing.*;

public abstract class AbstractGameFrame extends JFrame {

    private static final long serialVersionUID = 5810949416036712817L;

    /**
     * 按固定模板打开窗口
     *
     * @param config 窗口配置
     */
    protected final void openWindow(GameWindowConfig config) {
        this.beforeOpen();
        JPanel panel = new JPanel(config.getLayout());
        panel.setBackground(config.getBackground());
        this.buildContent(panel);
        this.showWindow(panel, config);
    }

    /**
     * 展示已构建好的面板，供需要先查询数据再展示的页面复用窗口收尾流程
     *
     * @param panel  已构建好的面板
     * @param config 窗口配置
     */
    protected final void showWindow(JPanel panel, GameWindowConfig config) {
        this.add(panel);
        this.setTitle(config.getTitle());
        this.setResizable(config.isResizable());
        this.setBounds(config.getX(), config.getY(), config.getWidth(), config.getHeight());
        Opt.ofNullable(config.getCloseOperation()).ifPresent(this::setDefaultCloseOperation);
        this.setVisible(true);
    }

    /**
     * 窗口打开前的准备钩子
     */
    protected void beforeOpen() {

    }

    /**
     * 构建页面内容，普通窗口只需要实现这一段业务布局
     *
     * @param panel 当前窗口的根面板
     */
    protected void buildContent(JPanel panel) {

    }

}
