package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 5462023615103520939L;

    /**
     *
     */
    private String userId;

    /**
     *
     */
    private String password;

    /**
     * 最佳成绩
     */
    private Integer highScore;

    /**
     * 最佳成绩完成时间
     */
    private Date highScoreAchievedTime;

    /**
     * 球的生命值
     */
    private Integer ballLife;

    /**
     * 球的大小
     */
    private Integer ballSize;

    /**
     * 通关所需消除块数
     */
    private Integer clearBrickCount;

    /**
     * 界面刷新帧率
     */
    private Integer fps;

}
