import java.io.Serializable;


public class SettingDTO implements Serializable {

    private Integer life;

    private Integer size;

    private Integer period;

    private Integer brickCount;

    private String userId;

    public Integer getLife() {
        return life;
    }

    public SettingDTO setLife(Integer life) {
        this.life = life;
        return this;
    }

    public Integer getSize() {
        return size;
    }

    public SettingDTO setSize(Integer size) {
        this.size = size;
        return this;
    }

    public Integer getPeriod() {
        return period;
    }

    public SettingDTO setPeriod(Integer period) {
        this.period = period;
        return this;
    }

    public Integer getBrickCount() {
        return brickCount;
    }

    public SettingDTO setBrickCount(Integer brickCount) {
        this.brickCount = brickCount;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public SettingDTO setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public String toString() {
        return "SettingDTO{" +
                "life=" + life +
                ", size=" + size +
                ", period=" + period +
                ", brickCount=" + brickCount +
                ", userId='" + userId + '\'' +
                '}';
    }
}
