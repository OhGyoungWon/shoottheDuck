package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

public class Pistol {
    private BufferedImage[] frames;   // 스프라이트 시트에서 잘라낸 프레임 저장
    private int currentFrame;         // 현재 재생 중인 프레임
    private boolean isShooting;       // 발사 애니메이션이 재생 중인지 여부
    private long animationStartTime;  // 애니메이션 시작 시간
    private int totalFrames;          // 스프라이트 시트의 총 프레임 수
    private long frameDuration;       // 각 프레임의 지속 시간

    public Pistol(BufferedImage spriteSheet, int totalFrames, long frameDuration) {
        this.totalFrames = totalFrames;
        this.frameDuration = frameDuration;
        frames = new BufferedImage[totalFrames];

        // 스프라이트 시트에서 프레임을 나누는 작업
        int frameWidth = spriteSheet.getWidth() / totalFrames;
        int frameHeight = spriteSheet.getHeight();
        for (int i = 0; i < totalFrames; i++) {
            frames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
        }
        reset();
    }

    // 발사 애니메이션 초기화
    public void reset() {
        currentFrame = 0;
        isShooting = false;
    }

    // 발사 애니메이션을 시작
    public void startShooting() {
        if (!isShooting) {
            isShooting = true;
            animationStartTime = System.nanoTime();
        }
    }

    // 애니메이션 업데이트
    public void update() {
        if (isShooting) {
            long elapsedTime = System.nanoTime() - animationStartTime;
            currentFrame = (int) (elapsedTime / frameDuration);

            // 애니메이션이 끝나면 발사 상태 종료
            if (currentFrame >= totalFrames) {
                reset();
            }
        }
    }

    // 현재 프레임의 이미지를 반환
    public BufferedImage getCurrentFrame() {
        return frames[currentFrame];
    }

    // 애니메이션이 재생 중인지 확인
    public boolean isShooting() {
        return isShooting;
    }
}
