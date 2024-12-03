package kr.jbnu.se.std;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DeadDuck {
    private final int x; // 오리의 X 좌표
    private int y; // 오리의 Y 좌표
    private final long startTime; // 표시 시작 시간
    private final long fadeDuration; // 페이드 아웃 지속 시간
    private final BufferedImage image; // 오리 이미지
    private float alpha; // 투명도 (0.0f = 완전 투명, 1.0f = 완전 불투명)

    public DeadDuck(int x, int y, BufferedImage image, long fadeDuration) {
        this.x = x;
        this.y = y;
        this.startTime = System.nanoTime();
        this.fadeDuration = fadeDuration; // 페이드 아웃 지속 시간 (예: 0.5초 = 500_000_000 나노초)
        this.image = image;
        this.alpha = 0.5f; // 초기 투명도
    }

    // 투명도를 업데이트
    public void update() {
        long elapsedTime = System.nanoTime() - startTime;
        alpha = Math.max(0.0f, 1.0f - (float) elapsedTime / fadeDuration); // 남은 투명도 계산
        y -= 1; // 위로 이동
    }

    // 페이드 아웃이 완료되었는지 확인
    public boolean isExpired() {
        return alpha <= 0.0f;
    }

    // 오리 그리기 (투명도 적용)
    public void draw(Graphics2D g2d) {
        if (image != null) {
            Composite originalComposite = g2d.getComposite(); // 원래의 Composite 저장
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)); // 투명도 설정
            g2d.drawImage(image, x, y, null);
            g2d.setComposite(originalComposite); // 원래의 Composite 복원
        }
    }
}
