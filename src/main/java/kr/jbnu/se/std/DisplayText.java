package kr.jbnu.se.std;

import java.awt.*;

public class DisplayText {
    private double x, y;
    private String text;
    private long startTime;
    private static final long DISPLAY_TIME = 1_000_000_000L; // 1초 동안 표시

    public DisplayText(double x, double y, String text) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.startTime = System.nanoTime();
    }

    // 데미지를 업데이트 (시간 경과에 따라 사라짐)
    public boolean update() {
        return System.nanoTime() - startTime < DISPLAY_TIME;
    }

    // 데미지 텍스트 그리기
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        g2d.drawString(text, (int)x, (int)y);
    }
}

