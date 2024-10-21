package kr.jbnu.se.std;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Itemduck {
    public static int[][] flylines = {
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.30)},
    };

    public int x;
    public int y;
    private float speed;
    public int score;
    public int hp;
    private BufferedImage flyImg;

    public Itemduck(int x, int y, int speed, int score, int hp, BufferedImage flyImg){
        this.x = x;
        this.y = y;
        this.hp = Game.getlvdata().duckhp*3;
        this.speed = Game.getlvdata().speed;
        this.score = Game.getlvdata().ducksc*2;
        this.flyImg = flyImg;
    }

    public void Update() {x += speed;}

    public void Draw(Graphics2D g2d){g2d.drawImage(flyImg, x, y, null);}

    public class chickenfly extends Itemduck{
        public chickenfly(int x, int y, int speed, int score, int hp, BufferedImage flyImg) {
            super(x, y, speed, score, hp, flyImg);
        }
    }
    public class cokefly extends Itemduck{
        public cokefly(int x, int y, int speed, int score, int hp, BufferedImage flyImg) {
            super(x, y, speed, score, hp, flyImg);
        }
    }
    public class pizzafly extends Itemduck {
        public pizzafly(int x, int y, int speed, int score, int hp, BufferedImage flyImg) {
            super(x, y, speed, score, hp, flyImg);
        }
    }
}
