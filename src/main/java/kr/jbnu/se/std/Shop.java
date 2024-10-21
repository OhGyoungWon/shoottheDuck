package kr.jbnu.se.std;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Shop {
    private ArrayList<ShopItem> shopItems;  // 아이템 목록
    private boolean isVisible;
    private Rectangle closeButtonBounds;

    public Shop() {
        shopItems = new ArrayList<>();
        isVisible = false;

        // 닫기 버튼 위치 설정
        closeButtonBounds = new Rectangle(400, 50, 40, 30);

        // 이미지 로드 예시 (이 부분은 적절한 이미지 경로로 변경해야 합니다)
        BufferedImage glovesImage = loadImage("/images/gloves.png");
        BufferedImage bulletImage = loadImage("/images/bullet.png");
        BufferedImage colaImage = loadImage("/images/cola.png");
        BufferedImage wineImage = loadImage("/images/wine.png");
        BufferedImage rubberDuckImage = loadImage("/images/rubberduck.png");

        // 아이템 추가
        shopItems.add(new ShopItem("장갑", 1000, glovesImage, "사실 고무장갑이랍니다     피해량 증가"));
        shopItems.add(new ShopItem("총알", 1500, bulletImage, "오리를 위한 깜짝선물    장탄수 증가"));
        shopItems.add(new ShopItem("콜라", 2000, colaImage, "제로콜라는 아니네요       체력 회복"));
        shopItems.add(new ShopItem("와인", 3000, wineImage, "문ㅇㅇ씨의 최애 음료수     오리 생성 속도 증가"));
        shopItems.add(new ShopItem("러버덕", 5000, rubberDuckImage, "적의 우두머리를 본딴 형상.       오리 생성 속도 감소"));
    }

    // 이미지 로드 함수
    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(this.getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 상점 화면을 그리는 함수
    public void drawShop(Graphics2D g2d) {
        if (!isVisible) return;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(50, 50, 400, 640);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("monospaced", Font.BOLD, 20));
        g2d.drawString("SHOP", 200, 80);

        // 닫기 버튼 그리기
        g2d.setColor(Color.RED);
        g2d.fillRect(closeButtonBounds.x, closeButtonBounds.y, closeButtonBounds.width, closeButtonBounds.height);
        g2d.setColor(Color.WHITE);
        g2d.drawString("X", closeButtonBounds.x + 15, closeButtonBounds.y + 20);

        // 아이템 그리기
        int x = 70;
        int y = 110;
        for (ShopItem shopItem : shopItems) {
            shopItem.drawItem(g2d, x, y);
            shopItem.drawDescription(g2d, x + 80, y + 20, 300);  // 아이템 설명을 오른쪽에 표시
            y += shopItem.getImage().getHeight() + 50;  // 다음 아이템 위치 설정
        }
    }

    // 마우스 클릭 처리 함수
    public void handleClick(Point mousePosition) {
        if (!isVisible) return;

        // 닫기 버튼 클릭 확인
        if (closeButtonBounds.contains(mousePosition)) {
            closeShop();
            return;
        }

        // 아이템 클릭 확인
        int x = 70;
        int y = 110;
        for (ShopItem shopItem : shopItems) {
            if (shopItem.isClicked(mousePosition, x, y)) {
                System.out.println("Bought: " + shopItem.getName());
                closeShop();
                break;
            }
            y += shopItem.getImage().getHeight() + 50;
        }
    }

    public void openShop() {
        isVisible = true;
    }

    public void closeShop() {
        isVisible = false;
    }

    public boolean isShopOpen() {
        return isVisible;
    }
}
