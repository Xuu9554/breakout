import dto.GameSetting;
import lombok.Data;

/**
 * 单局游戏运行状态
 */
@Data
public class BreakoutRoundState {

    /**
     * 当前游戏面板宽度
     */
    private int boardWidth;

    /**
     * 当前局刷新帧率
     */
    private int period;

    /**
     * 当前局得分
     */
    private int score;

    /**
     * 当前局通关所需消除块数
     */
    private int successCount;

    /**
     * 当前局剩余砖块数
     */
    private int remainingBrickCount;

    /**
     * 当前局是否暂停
     */
    private boolean pause;

    /**
     * 重置一局游戏的运行状态
     *
     * @param brickCount 当前局初始砖块总数
     */
    public void reset(int brickCount) {
        this.boardWidth = 0;
        this.period = 60;
        this.score = 0;
        this.successCount = 0;
        this.remainingBrickCount = brickCount;
        this.pause = true;
    }

    /**
     * 应用当前用户的游戏配置
     *
     * @param gameSetting 游戏配置
     * @param brickCount  当前局初始砖块总数
     */
    public void applyGameSetting(GameSetting gameSetting, int brickCount) {
        this.period = Math.max(1, gameSetting.getFps());
        this.successCount = Math.max(1, Math.min(gameSetting.getClearBrickCount(), brickCount));
    }

    /**
     * 当前得分扣除指定分数
     *
     * @param score 分数
     */
    public void deductScore(int score) {
        this.score -= score;
    }

    /**
     * 当前得分增加指定分数
     *
     * @param score 分数
     */
    public void addScore(int score) {
        this.score += score;
    }

    /**
     * 剩余砖块数减一
     */
    public void decreaseRemainingBrickCount() {
        this.remainingBrickCount--;
    }

    /**
     * 获取当前已消除砖块数
     *
     * @param brickCount 当前局初始砖块总数
     * @return int 已消除砖块数
     */
    public int getClearedBrickCount(int brickCount) {
        return brickCount - this.remainingBrickCount;
    }

    /**
     * 判断是否已经达到通关所需消除块数
     *
     * @param brickCount 当前局初始砖块总数
     * @return boolean 是否通关
     */
    public boolean hasClearedRequiredBricks(int brickCount) {
        return this.getClearedBrickCount(brickCount) >= this.successCount;
    }

    /**
     * 切换暂停状态
     */
    public void togglePause() {
        this.pause = !this.pause;
    }

}
