package kr.jbnu.se.std;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Weaponduck {
    Random random;
    public int x;
    public int y;
    private int speed;
    public int score;
    public int hp;
    public BufferedImage weadckImg;

    public Weaponduck(int x, int y, int speed, int score, int hp, BufferedImage weadckImg){
        this.x = x;
        this.y = y;
        this.hp = Game.getlvdata().bosshp/3;
        this.weadckImg = weadckImg;
    }

    public void Update(){x += speed;}

    public void Draw(Graphics2D g2d){g2d.drawImage(weadckImg, x, y, null);}

    public static class Smgduck extends Weaponduck{
        public Smgduck(int x, int y, int speed, int score, int hp, BufferedImage weadckImg) {
            super(x, y, speed, score, hp, weadckImg);
        }
    }
    public static class Rifduck extends Weaponduck{
        public Rifduck(int x, int y, int speed, int score, int hp, BufferedImage weadckImg) {
            super(x, y, speed, score, hp, weadckImg);
        }
    }
    public static class Odinduck extends Weaponduck{
        public Odinduck(int x, int y, int speed, int score, int hp, BufferedImage weadckImg) {
            super(x, y, speed, score, hp, weadckImg);
        }
    }
}
