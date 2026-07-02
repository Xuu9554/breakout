package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor(staticName = "of")
public class BreakoutGameResult implements Serializable {

    private static final long serialVersionUID = 7426524688187258023L;

    private final String title;

    private final int score;

}
