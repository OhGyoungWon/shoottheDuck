package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

//하고 사진 추가할거면 여기에 URL 선언해서 이미지 저장해놓고 추가해
//여기는 뭐 함수 추가할 건 아니고 그냥 스테이지 요소들 정보만 저장해놓고 Game클래스에서 여기있는거 불러서 쓸겨
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
    public static class lev1 extends Levels {
        public lev1() {
            super(15, 3, 20); // 원하면 수치 조정해도 됨
        }
    }
    public static class lev2 extends Levels{
        public lev2() {
            super(22, 5, 50);
        }
    }
    public static class lev3 extends Levels {
        public lev3() {
            super(26, 7, 70);
        }
    }
    public static class lev4 extends Levels {
        public lev4() {
            super(30, 8, 100);
        }
    }
}

