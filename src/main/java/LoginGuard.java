import ui.SwingOperation;

import javax.swing.*;
import java.awt.*;

public class LoginGuard {

    private static final String LOGIN_REQUIRED_MESSAGE = "请先登录！";

    private final Component parent;

    private LoginGuard(Component parent) {
        this.parent = parent;
    }

    /**
     * 创建绑定到当前入口组件的登录守卫
     *
     * @param parent 当前入口组件
     * @return {@link LoginGuard} 登录守卫
     */
    public static LoginGuard of(Component parent) {
        return new LoginGuard(parent);
    }

    /**
     * 已登录时执行目标操作，未登录时统一提示并按用户选择跳转登录页
     *
     * @param operation 需要登录后执行的操作
     * @throws Exception 操作执行失败时抛出异常，由SwingActionExecutor统一处理
     */
    public void run(SwingOperation operation) throws Exception {

        if (GameSupporter.loadCurrentLoggedInUserId().isPresent()) {
            operation.execute();
        } else {
            this.openLoginIfConfirmed();
        }
    }

    /**
     * 未登录时询问是否前往登录页
     */
    private void openLoginIfConfirmed() {

        int result = JOptionPane.showConfirmDialog(this.parent, LOGIN_REQUIRED_MESSAGE, "提示", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            if (this.parent instanceof Window) {
                this.parent.setVisible(false);
            }
            new Login();
        }
    }

}
