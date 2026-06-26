package dto;

import cn.hutool.core.lang.Opt;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class GameSetting implements Serializable {

    private static final long serialVersionUID = -2356009837518444920L;

    /**
     * 界面刷新帧率
     */
    private int fps;

    /**
     * 球的生命值
     */
    private int ballLife;

    /**
     * 球的大小
     */
    private int ballSize;

    /**
     * 通关所需消除块数
     */
    private int clearBrickCount;

    /**
     * @param ballLife        球的生命值
     * @param ballSize
     * @param clearBrickCount
     * @param fps
     * @return {@link GameSetting}
     */
    public static GameSetting of(int fps, int ballLife, int ballSize, int clearBrickCount) {
        return new GameSetting(fps, ballLife, ballSize, clearBrickCount);
    }

    /**
     * @param user
     * @return {@link GameSetting}
     */
    public static GameSetting from(User user) {
        return of(Opt.ofNullable(user.getFps()).orElse(DEFAULT_FPS),
                Opt.ofNullable(user.getBallLife()).orElse(DEFAULT_BALL_LIFE),
                Opt.ofNullable(user.getBallSize()).orElse(DEFAULT_BALL_SIZE),
                Opt.ofNullable(user.getClearBrickCount()).orElse(DEFAULT_CLEAR_BRICK_COUNT));
    }

    // ------------------------------------------------------------------------------------------------------------------------

    /**
     * 界面刷新帧率
     */
    private final static int DEFAULT_FPS = 60;

    /**
     * 球的生命值
     */
    private final static int DEFAULT_BALL_LIFE = 1;

    /**
     * 球的大小
     */
    private final static int DEFAULT_BALL_SIZE = 10;

    /**
     * 通关所需消除块数
     */
    private final static int DEFAULT_CLEAR_BRICK_COUNT = 10;

    public final static GameSetting DEFAULT_GAME_SETTING = of(DEFAULT_FPS, DEFAULT_BALL_LIFE, DEFAULT_BALL_SIZE, DEFAULT_CLEAR_BRICK_COUNT);

}
