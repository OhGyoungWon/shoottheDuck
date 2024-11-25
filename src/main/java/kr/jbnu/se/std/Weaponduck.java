package kr.jbnu.se.std;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Weaponduck {
    Random random;
    private int x;
    private int y;
    private float speed;
    private int score;
    private int hp;
    private BufferedImage weadckImg;

    public Weaponduck(int x, int y, float speed, int score, int hp, BufferedImage weadckImg){
        this.x = x;
        this.y = y;
        this.speed = Game.getlvdata().speed/2;
        this.score = score;
        this.hp = Game.getlvdata().bosshp/4;
        this.weadckImg = weadckImg;
    }

    public void Update() { x += speed; }

    public void Draw(Graphics2D g2d) {
        g2d.drawImage(weadckImg, x, y, null);
    }

    public static class Smgduck extends Weaponduck {
        public Smgduck(int x, int y, float speed, int score, int hp, BufferedImage weadckImg) {
            super(x, y, speed, score, hp, weadckImg);
        }
    }
    public static class Rifduck extends Weaponduck{
        public Rifduck(int x, int y, float speed, int score, int hp, BufferedImage weadckImg) {
            super(x, y, speed, score, hp, weadckImg);
        }
    }
    public static class Sniperduck extends Weaponduck{
        public Sniperduck(int x, int y, float speed, int score, int hp, BufferedImage weadckImg) {
            super(x, y, speed, score, hp, weadckImg);
        }
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public int getHp() { return hp; }

    public int getScore() { return score; }
}
