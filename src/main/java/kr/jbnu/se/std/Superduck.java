package kr.jbnu.se.std;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The duck class.
 *
 * @author www.gametutorial.net
 */

public class Superduck {
    public static int max;
    public static int hp;
    public static long timeBetweenDucks = 1_500_000_000;
    public static long lastDuckTime = 0;

    public static int[][] duckLines = {
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.60), -2, 30},  // 크기 30으로 변경
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.65), -3, 40},  // 크기 40으로 변경
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.70), -4, 50},  // 크기 50으로 변경
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.78), -5, 60}   // 크기 60으로 변경
    };
    /**
     * Indicate which is next duck line.
     */
    public static int nextDuckLines = 0;
    public int x;
    public int y;
    private final int speed;
    public int score;
    private final BufferedImage superduckImg;

    /**
     * Creates new duck.
     *
     * @param x Starting x coordinate.
     * @param y Starting y coordinate.
     * @param speed The speed of this duck.
     * @param score How many points this duck is worth?
     * @param superduckImg Image of the duck.
     */
    public Superduck(int x, int y, int speed, int score, BufferedImage superduckImg) {
        this.x = x;
        this.y = y;
        hp = 10;
        this.speed = speed/3;
        this.score = score+100;
        this.superduckImg = superduckImg;
    }
    public void Update()
    {
        x += speed;
    }
    /**
     * Draw the duck to the screen.
     * @param g2d Graphics2D
     */
    public void Draw(Graphics2D g2d) {
// Superduck 이미지 그리기
        g2d.drawImage(superduckImg, x, y,null);
// 체력바 그리기
        g2d.setColor(Color.RED); // 체력 바 배경 색상
        g2d.fillRect(x + 50, y - 20, 120, 10); // 이미지 위에 배경 그리기
        g2d.setColor(Color.GREEN); // 현재 체력 색상
        g2d.fillRect(x + 50, y - 20, 120 * hp / 10, 10); // 체력 바 그리기
    }
}
