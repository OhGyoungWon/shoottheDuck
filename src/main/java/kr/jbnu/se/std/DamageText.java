package kr.jbnu.se.std;

import java.awt.*;

public class DamageText {
    private int x, y;
    private int damage;
    private long startTime;
    private static final long DISPLAY_TIME = 1_000_000_000L; // 1초 동안 표시

    public DamageText(int x, int y, int damage) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.startTime = System.nanoTime();
    }

    // 데미지를 업데이트 (시간 경과에 따라 사라짐)
    public boolean update() {
        return System.nanoTime() - startTime < DISPLAY_TIME;
    }

    // 데미지 텍스트 그리기
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString(String.valueOf(damage), x, y);
    }
}

