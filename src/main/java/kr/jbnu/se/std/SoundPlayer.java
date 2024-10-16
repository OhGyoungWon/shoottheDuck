package kr.jbnu.se.std;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {
    // 사운드 파일들을 저장할 맵
    private Map<String, Clip> soundMap;

    public SoundPlayer() {
        soundMap = new HashMap<>();
    }

    // 사운드를 로드하는 메서드
    public void loadSound(String soundName, URL soundUrl) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundUrl);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            soundMap.put(soundName, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // 사운드를 재생하는 메서드
    public void play(String soundName) {
        Clip clip = soundMap.get(soundName);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();  // 이미 재생 중이면 멈춤
            }
            clip.setFramePosition(0);  // 시작 위치로 재설정
            clip.start();  // 사운드 재생
        } else {
            System.out.println("Sound not found: " + soundName);
        }
    }

    // 사운드를 정지하는 메서드
    public void stop(String soundName) {
        Clip clip = soundMap.get(soundName);
        if (clip != null && clip.isRunning()) {
            clip.stop();  // 사운드 정지
        }
    }
}
