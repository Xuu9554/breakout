package dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor(staticName = "of")
public class BrickRule implements Serializable {

    private static final long serialVersionUID = 6562794131501951736L;

    /**
     * 消除后获得的分数
     */
    private int score;

    /**
     * 初始生命值
     */
    private int life;

}
