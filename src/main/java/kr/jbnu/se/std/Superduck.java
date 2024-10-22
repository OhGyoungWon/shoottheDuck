package kr.jbnu.se.std;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The duck class.
 *
 * @author www.gametutorial.net
 */

public class Superduck {
    public static int max = Game.getlvdata().bosshp;
    public int hp;

    /**
     * Indicate which is next duck line.
     */
    public static int nextDuckLines = 0;
    public int x;
    public int y;
    private final float speed;
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
    public Superduck(int x, int y, float speed, int score, BufferedImage superduckImg) {
        this.x = x;
        this.y = y;
        hp = Game.getlvdata().bosshp;
        this.speed = Game.getlvdata().speed/3;
        this.score = Game.getlvdata().bosssc;
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
        g2d.drawImage(superduckImg, x, (int) (Framework.frameHeight*0.60),null);
// 체력바 그리기
        int max = Game.getlvdata().bosshp;
        g2d.setColor(Color.RED); // 체력 바 배경 색상
        g2d.fillRect(x - 20, (int)((Framework.frameHeight)*0.60)-21, 120, 10); // 이미지 위에 배경 그리기
        g2d.setColor(Color.GREEN); // 현재 체력 색상
        g2d.fillRect(x - 20, (int)((Framework.frameHeight)*0.60)-21, (int) (120 * ((float)hp / max)), 10); // 체력 바 그리기
    }
}
