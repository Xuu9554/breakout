package view;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import dto.GameSetting;
import exception.ServiceAssert;
import support.BreakoutGameContext;
import support.GameSupporter;
import ui.*;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

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
     * 配置保存后的附加动作
     */
    private final Consumer<GameSetting> settingAppliedAction;

    /**
     * 打开游戏设置窗口
     */
    public GameSettingsFrame() {
        this(null);
    }

    /**
     * 打开游戏设置窗口，并在保存后执行额外动作
     *
     * @param settingAppliedAction 保存后的配置回调
     */
    public GameSettingsFrame(Consumer<GameSetting> settingAppliedAction) {
        this.settingAppliedAction = settingAppliedAction;
        this.openWindow(GameWindowConfig.of("Game Settings", 650, 330, 380, 280));
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
        formFactory.label(StrUtil.format("建议输入1-{}之间的整数", MAX_CLEAR_BRICK_COUNT), 170, 130, 180, 30, Color.RED);

        SwingActionFactory.with(this).bind(formFactory.button("Apply", 230, 180, 110, 30, Color.GREEN), this::applySetting);
    }

    /**
     * 保存用户游戏配置
     */
    private void applySetting() {

        int realBallLife = this.readUnsignedInteger(this.ballLife, "小球生命不合法");
        int realBallSize = this.readUnsignedInteger(this.ballSize, "小球大小不合法");
        int realFps = this.readUnsignedInteger(this.fps, "画面刷新帧率不合法");
        int realClearBrickCount = this.readUnsignedInteger(this.clearBrickCount, "通关块数不合法");

        GameSetting gameSetting = GameSetting.of(clampValue(realFps, MIN_FPS, MAX_FPS), Math.max(realBallLife, MIN_BALL_LIFE),
                clampValue(realBallSize, MIN_BALL_SIZE, MAX_BALL_SIZE), clampValue(realClearBrickCount, MIN_CLEAR_BRICK_COUNT, MAX_CLEAR_BRICK_COUNT));

        GameSupporter.updateCurrentUserGameSetting(gameSetting);

        if (!ObjectUtil.isNull(this.settingAppliedAction)) {
            this.settingAppliedAction.accept(gameSetting);
        }

        String message = StrUtil.format(
                "修改参数成功~{}！\n生命: {}\n小球大小: {}\n刷新频率: {}\n通关块数: {}",
                ObjectUtil.isNull(this.settingAppliedAction) ? "下局生效" : "当前局已生效",
                gameSetting.getBallLife(), gameSetting.getBallSize(), gameSetting.getFps(), gameSetting.getClearBrickCount());

        SwingDialogs.information(this, message, "操作成功！");
        SwingWindows.hide(this);
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

    /**
     * 非负整数正则
     */
    private final static Pattern UNSIGNED_INTEGER = Pattern.compile("[0-9]+");

    /**
     * 小球最小生命
     */
    private final static int MIN_BALL_LIFE = 1;

    /**
     * 小球最小尺寸
     */
    private final static int MIN_BALL_SIZE = 1;

    /**
     * 小球最大尺寸
     */
    private final static int MAX_BALL_SIZE = 18;

    /**
     * 最小刷新频率
     */
    private final static int MIN_FPS = 1;

    /**
     * 最大刷新频率
     */
    private final static int MAX_FPS = 100;

    /**
     * 最小通关块数
     */
    private final static int MIN_CLEAR_BRICK_COUNT = 1;

    /**
     * 最大通关块数
     */
    private final static int MAX_CLEAR_BRICK_COUNT = BreakoutGameContext.BRICK_COUNT;

}
