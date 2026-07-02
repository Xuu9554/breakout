package dto;

import cn.hutool.core.lang.Opt;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor(staticName = "of")
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
     * 构造
     *
     * @param user 用户信息
     * @return {@link GameSetting} 游戏设置
     */
    public static GameSetting from(User user) {
        return of(Opt.ofNullable(user.getFps()).orElse(60),
                Opt.ofNullable(user.getBallLife()).orElse(1),
                Opt.ofNullable(user.getBallSize()).orElse(10),
                Opt.ofNullable(user.getClearBrickCount()).orElse(10));
    }

}
