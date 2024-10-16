package kr.jbnu.se.std;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {

    private Clip clip;

    // 사운드 파일 로드
    public SoundPlayer(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath); // 사운드 파일 경로
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // 사운드 재생
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // 처음부터 재생
            clip.start();
        }
    }

    // 사운드 중지
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}

