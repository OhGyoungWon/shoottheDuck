package kr.jbnu.se.std;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The duck class.
 *
 * @author www.gametutorial.net
 */

public class Superduck {
    private int max;
    private final int hp;

    /**
     * Indicate which is next duck line.
     */
    private int x;  // getX랑 setX 클래스 만들어서 x 외부에서 사용하기. 나머지도 변수들도 동일
    private final int y;
    private final float speed;
    private int score;
    private final BufferedImage superduckImg;

    /**
     * Creates new duck.
     *
     * @param x Starting x coordinate.
     * @param y Starting y coordinate.
     * @param superduckImg Image of the duck.
     */
    public Superduck(int x, int y, BufferedImage superduckImg) {
        this.x = x;
        this.y = y;
        hp = Game.getlvdata().bosshp;
        this.speed = Game.getlvdata().speed/3;
        this.score = Game.getlvdata().bosssc;
        this.superduckImg = superduckImg;
        max = Game.getlvdata().bosshp;
    }

    /**
     * x 값 업데이트
     */
    public void Update()
    {
        x += (int) speed;
    }
    /**
     * Draw the duck to the screen.
     * @param g2d Graphics2D
     */
    public void Draw(Graphics2D g2d) {
        // Superduck 이미지 그리기
        g2d.drawImage(superduckImg, x, (int) (Framework.frameHeight*0.60),null);
        // 체력바 그리기
        max = Game.getlvdata().bosshp;
        g2d.setColor(Color.RED); // 체력 바 배경 색상
        g2d.fillRect(x - 20, (int)((Framework.frameHeight)*0.60)-21, 120, 10); // 이미지 위에 배경 그리기
        g2d.setColor(Color.GREEN); // 현재 체력 색상
        g2d.fillRect(x - 20, (int)((Framework.frameHeight)*0.60)-21, (int) (120 * ((float)hp / max)), 10); // 체력 바 그리기
    }

    public int getHp(){
        return hp;
    }

    public int getScore() {
        return score;
    }

    public int getX(){
        return x;
    }

    public int getY() {
        return y;
    }
}
