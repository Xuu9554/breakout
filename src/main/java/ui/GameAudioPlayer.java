package ui;

import cn.hutool.core.util.ObjectUtil;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GameAudioPlayer implements AutoCloseable {

    /**
     * 当前音频片段
     */
    private Clip clip;

    /**
     * 当前音频是否循环播放
     */
    private boolean looping;

    /**
     * 当前音频是否处于暂停状态
     */
    private boolean paused;

    /**
     * 循环播放音频
     */
    public void loop(String audioResourcePath) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.open(audioResourcePath);
        this.looping = true;
        this.paused = false;
        this.clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * 播放一次音频
     */
    public void playOnce(String audioResourcePath) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.open(audioResourcePath);
        this.looping = false;
        this.paused = false;
        this.clip.start();
    }

    /**
     * 暂停当前音频
     */
    public void pause() {

        if (!ObjectUtil.isNull(this.clip) && this.clip.isRunning()) {
            this.clip.stop();
            this.paused = true;
        }
    }

    /**
     * 恢复当前音频
     */
    public void resume() {

        if (ObjectUtil.isNull(this.clip) || !this.paused) {
            return;
        }

        this.paused = false;

        if (this.looping) {
            this.clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            this.clip.start();
        }
    }

    /**
     * 停止当前音频
     */
    public void stop() {

        if (ObjectUtil.isNull(this.clip)) {
            return;
        }

        this.clip.stop();
        this.clip.setFramePosition(0);
        this.paused = false;
    }

    @Override
    public void close() {

        if (ObjectUtil.isNull(this.clip)) {
            return;
        }

        this.clip.stop();
        this.clip.close();
        this.clip = null;
        this.looping = false;
        this.paused = false;
    }

    /**
     * 打开音频文件
     *
     * @param audioResourcePath 音频资源路径
     */
    private void open(String audioResourcePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        this.close();

        try (
                InputStream audioInputStream = GameAudioPlayer.class.getResourceAsStream(audioResourcePath);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(audioInputStream);
                AudioInputStream clipInputStream = AudioSystem.getAudioInputStream(bufferedInputStream)
        ) {
            Clip newClip = AudioSystem.getClip();
            newClip.open(clipInputStream);
            this.clip = newClip;
        }
    }

}
