package dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class BreakoutGameResult implements Serializable {

    private static final long serialVersionUID = 7426524688187258023L;

    private final String title;

    private final int score;

    private BreakoutGameResult(String title, int score) {
        this.title = title;
        this.score = score;
    }

    /**
     * 创建游戏结果
     *
     * @param title 结果标题
     * @param score 当前成绩
     * @return {@link BreakoutGameResult} 游戏结果
     */
    public static BreakoutGameResult of(String title, int score) {
        return new BreakoutGameResult(title, score);
    }

}
