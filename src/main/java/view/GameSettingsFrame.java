package view;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import dto.GameSetting;
import exception.ServiceAssert;
import support.GameSupporter;
import ui.*;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

import static support.BreakoutGameContext.BRICK_COUNT;
import static ui.GameFonts.FORM_TEXT;

public class GameSettingsFrame extends AbstractGameFrame {

    private static final long serialVersionUID = -8966949405858572793L;

    /**
     * 小球生命输入框
     */
    private final JTextField ballLife = new JTextField();

    /**
     * 小球大小输入框
     */
    private final JTextField ballSize = new JTextField();

    /**
     * 刷新频率输入框
     */
    private final JTextField fps = new JTextField();

    /**
     * 通关块数输入框
     */
    private final JTextField clearBrickCount = new JTextField();

    /**
     * 打开游戏设置窗口
     */
    public GameSettingsFrame() {
        this.openWindow(GameWindowConfig.of("游戏设置", WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT).setBackground(PANEL_BACKGROUND));
    }

    /**
     * 构建游戏设置表单
     *
     * @param panel 当前窗口的根面板
     */
    @Override
    protected void buildContent(JPanel panel) {

        SwingFormFactory formFactory = SwingFormFactory.with(panel, FORM_TEXT);
        GameSetting gameSetting = GameSupporter.loadCurrentUserGameSetting();

        JLabel title = formFactory.label("游戏设置", 0, 30, WINDOW_WIDTH, 42, TITLE_COLOR);
        title.setFont(TITLE_FONT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        formFactory.label("小球生命", 72, 98, 90, 30, LABEL_TEXT_COLOR);
        formFactory.component(this.ballLife, 168, 98, 58, 30);
        decorateTextField(this.ballLife);
        this.ballLife.setText(String.valueOf(gameSetting.getBallLife()));
        formFactory.label("小球生命至少为一次", 244, 98, 210, 30, HINT_TEXT_COLOR);

        formFactory.label("小球大小", 72, 140, 90, 30, LABEL_TEXT_COLOR);
        formFactory.component(this.ballSize, 168, 140, 58, 30);
        decorateTextField(this.ballSize);
        this.ballSize.setText(String.valueOf(gameSetting.getBallSize()));
        formFactory.label("建议输入8-18之间的整数", 244, 140, 210, 30, HINT_TEXT_COLOR);

        formFactory.label("刷新频率", 72, 182, 90, 30, LABEL_TEXT_COLOR);
        formFactory.component(this.fps, 168, 182, 58, 30);
        decorateTextField(this.fps);
        this.fps.setText(String.valueOf(gameSetting.getFps()));
        formFactory.label("建议刷新率在60-80之间", 244, 182, 210, 30, HINT_TEXT_COLOR);

        formFactory.label("通关块数", 72, 224, 90, 30, LABEL_TEXT_COLOR);
        formFactory.component(this.clearBrickCount, 168, 224, 58, 30);
        decorateTextField(this.clearBrickCount);
        this.clearBrickCount.setText(String.valueOf(gameSetting.getClearBrickCount()));
        formFactory.label(StrUtil.format("建议输入1-{}之间的整数", BRICK_COUNT), 244, 224, 210, 30, HINT_TEXT_COLOR);

        JButton applyButton = formFactory.button(new ArcadeMenuButton("保存设置"), 178, 302, 144, 42, BUTTON_BLUE);
        applyButton.setForeground(Color.WHITE);
        applyButton.setFont(FORM_TEXT);
        applyButton.setFocusPainted(false);
        applyButton.setBorderPainted(false);

        SwingActionFactory.with(this).bind(applyButton, this::applySetting);
    }

    /**
     * 保存用户游戏配置
     */
    private void applySetting() {

        int realBallLife = readUnsignedInteger(this.ballLife, "小球生命不合法");
        int realBallSize = readUnsignedInteger(this.ballSize, "小球大小不合法");
        int realFps = readUnsignedInteger(this.fps, "画面刷新帧率不合法");
        int realClearBrickCount = readUnsignedInteger(this.clearBrickCount, "通关块数不合法");

        GameSetting gameSetting = GameSetting.of(clampValue(realFps, 100), Math.max(realBallLife, 1),
                clampValue(realBallSize, 18), clampValue(realClearBrickCount, BRICK_COUNT));

        GameSupporter.updateCurrentUserGameSetting(gameSetting);

        String message = StrUtil.format("修改参数成功~\n生命: {}\n小球大小: {}\n刷新频率: {}\n通关块数: {}",
                gameSetting.getBallLife(), gameSetting.getBallSize(), gameSetting.getFps(), gameSetting.getClearBrickCount());

        SwingDialogs.information(this, message, "操作成功！");
        SwingWindows.hide(this);
    }

    /**
     * 装饰设置项输入框
     *
     * @param textField 输入框
     */
    private static void decorateTextField(JTextField textField) {
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setForeground(LABEL_TEXT_COLOR);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createLineBorder(FIELD_BORDER_COLOR));
    }

    /**
     * 读取非负整数输入
     *
     * @param textField    输入框
     * @param errorMessage 错误提示
     * @return int 非负整数
     */
    private static int readUnsignedInteger(JTextField textField, String errorMessage) {
        ServiceAssert.isTrue(ReUtil.isMatch(UNSIGNED_INTEGER, textField.getText()), errorMessage);
        return Integer.parseInt(textField.getText());
    }

    /**
     * 将整数规整到取值范围内
     *
     * @param value 输入值
     * @param max   最大值
     * @return int 规整后的值
     */
    private static int clampValue(int value, int max) {
        return Math.max(1, Math.min(value, max));
    }

    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 非负整数正则
     */
    private final static Pattern UNSIGNED_INTEGER = Pattern.compile("[0-9]+");

    /**
     * 设置窗口横坐标
     */
    private final static int WINDOW_X = 650;

    /**
     * 设置窗口纵坐标
     */
    private final static int WINDOW_Y = 250;

    /**
     * 设置窗口宽度
     */
    private final static int WINDOW_WIDTH = 500;

    /**
     * 设置窗口高度
     */
    private final static int WINDOW_HEIGHT = 430;

    /**
     * 设置页标题字体
     */
    private final static Font TITLE_FONT = new Font("黑体", Font.BOLD, 30);

    /**
     * 设置项提示颜色
     */
    private final static Color HINT_TEXT_COLOR = new Color(84, 110, 122);

    /**
     * 设置页背景色
     */
    private final static Color PANEL_BACKGROUND = new Color(248, 250, 252);

    /**
     * 设置页标题颜色
     */
    private final static Color TITLE_COLOR = new Color(42, 107, 255);

    /**
     * 设置项标题颜色
     */
    private final static Color LABEL_TEXT_COLOR = new Color(38, 50, 56);

    /**
     * 设置项输入框边框颜色
     */
    private final static Color FIELD_BORDER_COLOR = new Color(207, 216, 220);

    /**
     * 设置页按钮蓝色
     */
    private final static Color BUTTON_BLUE = new Color(42, 107, 255);

}
