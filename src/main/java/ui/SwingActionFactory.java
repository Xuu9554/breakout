package ui;

import javax.swing.*;
import java.awt.*;

public class SwingActionFactory {

    private final Component parent;

    private SwingActionFactory(Component parent) {
        this.parent = parent;
    }

    /**
     * 创建绑定到父组件的动作工厂
     *
     * @param parent 父组件
     * @return {@link SwingActionFactory} 动作工厂
     */
    public static SwingActionFactory with(Component parent) {
        return new SwingActionFactory(parent);
    }

    /**
     * 绑定按钮动作
     *
     * @param button    按钮
     * @param operation Swing操作
     * @return {@link SwingActionFactory} 动作工厂
     */
    public SwingActionFactory bind(AbstractButton button, SwingOperation operation) {
        button.addActionListener(event -> SwingActionExecutor.execute(parent, operation));
        return this;
    }

}
