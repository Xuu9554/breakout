import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import dto.GameSetting;
import exception.ServiceAssert;
import ui.SwingActionFactory;
import ui.SwingFormFactory;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

public class Setting extends JFrame {

    private static final long serialVersionUID = -8966949405858572793L;

    private final JTextField ballLife = new JTextField();

    private final JTextField ballSize = new JTextField();

    private final JTextField fps = new JTextField();

    private final JTextField clearBrickCount = new JTextField();

    public Setting() {

        JPanel settingPanel = new JPanel();
        settingPanel.setLayout(null);
        settingPanel.setBackground(Color.WHITE);

        SwingFormFactory formFactory = SwingFormFactory.with(settingPanel, new Font("黑体", Font.BOLD, 15));
        GameSetting gameSetting = GameSupporter.loadCurrentUserGameSetting();

        formFactory.label("小球生命", 30, 25, 180, 30);
        formFactory.component(this.ballLife, 110, 25, 50, 30);
        this.ballLife.setText(String.valueOf(gameSetting.getBallLife()));
        formFactory.label("小球生命至少为一次", 170, 25, 180, 30, Color.RED);

        formFactory.label("小球大小", 30, 60, 180, 30);
        formFactory.component(this.ballSize, 110, 60, 50, 30);
        this.ballSize.setText(String.valueOf(gameSetting.getBallSize()));
        formFactory.label("建议输入8-18之间的整数", 170, 60, 180, 30, Color.RED);

        formFactory.label("刷新频率", 30, 95, 180, 30);
        formFactory.component(this.fps, 110, 95, 50, 30);
        this.fps.setText(String.valueOf(gameSetting.getFps()));
        formFactory.label("建议刷新率在60-80之间", 170, 95, 180, 30, Color.RED);

        formFactory.label("通关块数", 30, 130, 180, 30);
        formFactory.component(this.clearBrickCount, 110, 130, 50, 30);
        this.clearBrickCount.setText(String.valueOf(gameSetting.getClearBrickCount()));
        formFactory.label("建议输入0-99之间的整数", 170, 130, 180, 30, Color.RED);

        SwingActionFactory.with(this).bind(formFactory.button("Apply", 230, 180, 110, 30, Color.GREEN), this::applySetting);

        this.add(settingPanel);
        this.setTitle("Game Setting");
        this.setResizable(false);
        this.setBounds(650, 330, 380, 280);
        this.setVisible(true);
    }

    /**
     * 保存用户游戏配置
     */
    private void applySetting() {

        int realBallLife = this.readUnsignedInteger(this.ballLife, "小球生命不合法");
        int realBallSize = this.readUnsignedInteger(this.ballSize, "小球大小不合法");
        int realFps = this.readUnsignedInteger(this.fps, "画面刷新帧率不合法");
        int realClearBrickCount = this.readUnsignedInteger(this.clearBrickCount, "通关块数不合法");

        GameSetting gameSetting = GameSetting.of(Math.min(realFps, MAX_FPS), Math.max(realBallLife, MIN_BALL_LIFE),
                clampValue(realBallSize, MIN_BALL_SIZE, MAX_BALL_SIZE), clampValue(realClearBrickCount, MIN_CLEAR_BRICK_COUNT, MAX_CLEAR_BRICK_COUNT));

        GameSupporter.updateCurrentUserGameSetting(gameSetting);

        String message = StrUtil.format(
                "修改参数成功~下局生效！\n生命: {}\n小球大小: {}\n刷新频率: {}\n通关块数: {}", realBallLife, realBallSize, realFps, realClearBrickCount);

        JOptionPane.showMessageDialog(this, message, "操作成功！", JOptionPane.INFORMATION_MESSAGE);
        this.setVisible(false);
    }

    /**
     * 读取非负整数输入
     *
     * @param textField    输入框
     * @param errorMessage 错误提示
     * @return int 非负整数
     */
    private int readUnsignedInteger(JTextField textField, String errorMessage) {
        ServiceAssert.isTrue(ReUtil.isMatch(UNSIGNED_INTEGER, textField.getText()), errorMessage);
        return Integer.parseInt(textField.getText());
    }

    /**
     * 将整数规整到取值范围内
     *
     * @param value 输入值
     * @param min   最小值
     * @param max   最大值
     * @return int 规整后的值
     */
    private static int clampValue(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    // ------------------------------------------------------------------------------------------------------------------------

    private final static Pattern UNSIGNED_INTEGER = Pattern.compile("[0-9]+");

    private static final int MIN_BALL_LIFE = 1;

    private static final int MIN_BALL_SIZE = 1;

    private static final int MAX_BALL_SIZE = 18;

    private static final int MAX_FPS = 100;

    private static final int MIN_CLEAR_BRICK_COUNT = 0;

    private static final int MAX_CLEAR_BRICK_COUNT = 99;

}
