package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

//하고 사진 추가할거면 여기에 URL 선언해서 이미지 저장해놓고 추가해
//여기는 뭐 함수 추가할 건 아니고 그냥 스테이지 요소들 정보만 저장해놓고 Game클래스에서 여기있는거 불러서 쓸겨
public class Levels {
    public float speed;
    public int duckhp;//오리 체력
    public int bosshp;//보스 체력
    public int ducksc;//오리 점수
    public int bosssc;//보스 점수
    public int sumdly;//소환 속도 조절
    public Levels(float speed, int duckhp, int bosshp, int ducksc, int sumdly) {
        this.speed = speed;
        this.duckhp = duckhp;
        this.bosshp = bosshp;
        this.ducksc = ducksc;
        this.bosssc = ducksc*5;
        this.sumdly = sumdly;
    }
    public static class Lev1 extends Levels {
        public Lev1() {
            super(-2, 2, 20, 30, 1_000_000_000); // 원하면 수치 조정해도 됨
        }
    }
    public static class Lev2 extends Levels{
        public Lev2() {
            super(-3, 4, 60, 45, 800_000_000);
        }
    }
    public static class Lev3 extends Levels {
        public Lev3() {
            super(-4, 6, 100, 60, 600_000_000);
        }
    }
    public static class Lev4 extends Levels {
        public Lev4() {
            super(-5, 8, 120, 75, 400_000_000); }
    }
    public static class Lev5 extends Levels {
        public Lev5() {super(-5, 12, 200, 120, 300_000_000); }
    }
}

