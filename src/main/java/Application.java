import db.DataSourceBootstrap;
import ui.SwingActionExecutor;
import view.MainMenuFrame;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {
        SwingActionExecutor.execute(null, () -> {
            DataSourceBootstrap.initialize();
            SwingUtilities.invokeLater(() -> SwingActionExecutor.execute(null, MainMenuFrame::new));
        });
    }

}
