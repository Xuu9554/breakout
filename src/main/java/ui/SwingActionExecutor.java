package ui;

import exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

@Slf4j
public class SwingActionExecutor {

    public static void execute(Component parent, SwingOperation operation) {
        try {
            operation.execute();
        } catch (ServiceException exception) {
            SwingDialogs.warning(parent, exception.getMessage());
        } catch (Exception e) {
            log.error("Swing操作执行失败: {}", e.getMessage(), e);
            SwingDialogs.error(parent, "程序发生异常, 请稍后重试");
        }
    }

}
