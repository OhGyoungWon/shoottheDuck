package kr.jbnu.se.std;

import java.awt.image.BufferedImage;
import java.net.URL;

public class Levels {
    private int speed;
    private int duckhp;
    private int bosshp;
    public BufferedImage background;
    public BufferedImage duck;
    public BufferedImage boss;
    public Levels(int speed, int duckhp, int bosshp) {
        this.speed = speed;
        this.duckhp = duckhp;
        this.bosshp = bosshp;
    }
    public class lev1 extends Levels {
        public lev1(int speed, int duckhp, int bosshp) {
            super(speed, duckhp, bosshp);
        }
    }
    public class lev2 extends Levels {
        public lev2(int speed, int duckhp, int bosshp) {
            super(speed, duckhp, bosshp);
        }
    }
}

