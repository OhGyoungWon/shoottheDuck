package kr.jbnu.se.std;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Weapon {
    protected BufferedImage gunImage; // 무기 이미지
    protected int damage; // 피해량
    protected long fireDelay; // 발사 딜레이 (밀리초 단위)
    protected int ammos;

    public Weapon(BufferedImage gunImage, int damage, long fireDelay, int ammos) {
        this.gunImage = gunImage;
        this.damage = damage;
        this.fireDelay = fireDelay;
        this.ammos = ammos;
    }

    public int getDamage() {
        return damage;
    }

    public long getFireDelay() {
        return fireDelay;
    }

    public abstract String getName(); // 무기 이름을 반환하는 추상 메소드

    public void draw(Graphics2D g2d, int panelWidth) {
        // 무기 이미지 그리기
        int imageX = panelWidth - gunImage.getWidth() - 20; // 우측 끝에서 이미지 넓이만큼 띄우기
        int imageY = 20;  // 텍스트 아래쪽에 이미지 배치

        g2d.drawImage(gunImage, imageX, imageY, 40, 40, null); // 높이 40으로 줄여서 출력
    }

    public static class Revolver extends Weapon {//기본 무기
        public Revolver(BufferedImage gunImage) {
            super(gunImage, 2, 1_000_000_000, 6); // 피해량 2, 발사 딜레이 1second
        }

        @Override
        public String getName() {
            return "Revolver";
        }
    }

    public class SMG extends Weapon {
        public SMG(BufferedImage gunImage) {
            super(gunImage, 1, 100_000_000, 20); // 피해량 1, 발사 딜레이 0.1초
        }

        @Override
        public String getName() {
            return "SMG";
        }
    }

    public class Rifle extends Weapon {
        public Rifle(BufferedImage gunImage) {
            super(gunImage, 3, 200_000_000, 25); // 피해량 35, 발사 딜레이 0.2초
        }

        @Override
        public String getName() {
            return "Rifle";
        }
    }

    public class Odin extends Weapon {
        public Odin(BufferedImage gunImage) {
            super(gunImage, 5, 200_000_000, 70); // 피해량 5, 발사 딜레이 0.2초
        }

        @Override
        public String getName() {
            return "Odin";
        }

    }
}
