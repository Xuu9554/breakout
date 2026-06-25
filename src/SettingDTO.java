import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class SettingDTO implements Serializable {

    private static final long serialVersionUID = -6648501639112088941L;

    private Integer life;

    private Integer size;

    private Integer period;

    private Integer brickCount;

    private String userId;

}
