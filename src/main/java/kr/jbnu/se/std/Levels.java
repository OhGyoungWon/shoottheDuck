package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

//하고 사진 추가할거면 여기에 URL 선언해서 이미지 저장해놓고 추가해
//여기는 뭐 함수 추가할 건 아니고 그냥 스테이지 요소들 정보만 저장해놓고 Game클래스에서 여기있는거 불러서 쓸겨
public class Levels {
    public int speed;
    public int duckhp;
    public int bosshp;
    public int ducksc;
    public int bosssc;
    public int sumdly;//소환 속도 조절
    public BufferedImage background;
    public BufferedImage duck;
    public BufferedImage boss;
    public Levels(int speed, int duckhp, int bosshp, int ducksc, int sumdly) {
        this.speed = speed;
        this.duckhp = duckhp;
        this.bosshp = bosshp;
        this.ducksc = ducksc;
        this.bosssc = ducksc*5;
        this.sumdly = sumdly;
    }
    public static class lev1 extends Levels {
        public lev1() {
            super(-2, 3, 20, 30, 1_000_000_000); // 원하면 수치 조정해도 됨
        }
    }
    public static class lev2 extends Levels{
        public lev2() {
            super(-3, 5, 50, 45, 900_000_000);
        }
    }
    public static class lev3 extends Levels {
        public lev3() {
            super(-5, 6, 70, 60, 800_000_000);
        }
    }
    public static class lev4 extends Levels {
        public lev4() {
            super(-7, 8, 100, 70, 700_000_000);
        }
    }
}

