package ui;

import exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;

@Slf4j
public class SwingActionExecutor {

    public static void execute(Component parent, SwingOperation operation) {
        try {
            operation.execute();
        } catch (ServiceException exception) {
            JOptionPane.showMessageDialog(parent, exception.getMessage(), "提示", WARNING_MESSAGE);
        } catch (Exception e) {
            log.error("Swing操作执行失败: {}", e.getMessage(), e);
            JOptionPane.showMessageDialog(parent, "程序发生异常, 请稍后重试", "错误", ERROR_MESSAGE);
        }
    }

}
