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
    private Rectangle glovesBounds; // 장갑 이미지 범위
    private Rectangle bulletBounds; // 총알 이미지 범위
    private Rectangle colaBounds; // 콜라 이미지 범위
    private Rectangle wineBounds; // 와인 이미지 범위
    private Rectangle rubberDuckBounds; // 러버덕 이미지 범위

    private String purchaseMessage = "";  // 구매 메시지 저장 변수
    private long messageDisplayTime = 0;  // 메시지 표시 시간
    private static long MESSAGE_DURATION = 2_000_000_000;  // 메시지 표시 시간 (밀리초)

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
        shopItems.add(new ShopItem("장갑", 200, glovesImage, "사실 고무장갑이랍니다     피해량 증가"));
        shopItems.add(new ShopItem("총알", 300, bulletImage, "오리를 위한 깜짝선물    장전시간 감소"));
        shopItems.add(new ShopItem("콜라", 500, colaImage, "제로콜라는 아니네요       체력 회복"));
        shopItems.add(new ShopItem("와인", 1000, wineImage, "문ㅇㅇ씨의 최애 음료수     사격 속도 증가"));
        shopItems.add(new ShopItem("러버덕", 2000, rubberDuckImage, "적의 우두머리를 본딴 형상.       R키로 특수 스킬"));

        setupItemBounds(glovesImage, bulletImage, colaImage, wineImage, rubberDuckImage);
    }
    private void setupItemBounds(BufferedImage glovesImage, BufferedImage bulletImage, BufferedImage colaImage, BufferedImage wineImage, BufferedImage rubberDuckImage) {
        int x = 70; // 아이템 X 좌표 시작점
        int y = 110; // 아이템 Y 좌표 시작점

        // 장갑 이미지 범위
        glovesBounds = new Rectangle(x, y, glovesImage.getWidth(), glovesImage.getHeight());
        y += glovesImage.getHeight() + 50; // 다음 아이템을 위해 Y 좌표 이동

        // 총알 이미지 범위
        bulletBounds = new Rectangle(x, y, bulletImage.getWidth(), bulletImage.getHeight());
        y += bulletImage.getHeight() + 50;

        // 콜라 이미지 범위
        colaBounds = new Rectangle(x, y, colaImage.getWidth(), colaImage.getHeight());
        y += colaImage.getHeight() + 50;

        // 와인 이미지 범위
        wineBounds = new Rectangle(x, y, wineImage.getWidth(), wineImage.getHeight());
        y += wineImage.getHeight() + 50;

        // 러버덕 이미지 범위
        rubberDuckBounds = new Rectangle(x, y, rubberDuckImage.getWidth(), rubberDuckImage.getHeight());
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

        // 장갑 클릭 확인
        if (glovesBounds.contains(mousePosition)) {
            // 장갑 아이템 클릭 처리 로직 추가
            if(Game.getMoney() > 200){
                Game.reduceMoney(200);
                Game.setAdiatt(Game.getAdiatt() + 3);
                Purchase("장갑");
            }
            else{
                Lessmoney();
            }
            return;
        }

        // 총알 클릭 확인
        if (bulletBounds.contains(mousePosition)) {//여기 로직 구현해야돼 장전을 구현해야 이걸 할수가 있어
            // 총알 아이템 클릭 처리 로직 추가
            if(Game.getMoney() > 300){
                Game.reduceMoney(300);
                if(Game.getReloadDuration() <= 300_000_000){
                    Game.setReloadDuration(300_000_000);
                }
                else{
                    Game.setReloadDuration(Game.getReloadDuration() - 300_000_000);
                }
                Purchase("총알");
            }
            else{
                Lessmoney();
            }
            return;
        }

        // 콜라 클릭 확인
        if (colaBounds.contains(mousePosition)) {
            // 콜라 아이템 클릭 처리 로직 추가
            if(Game.getMoney() > 500){
                Game.reduceMoney(500);
                if(Game.getRunawayDucks() < 20){
                    Game.reduceRunawayDucks(Game.getRunawayDucks());
                }
                else{
                    Game.reduceRunawayDucks(Game.getRunawayDucks() - 20);
                }
                Purchase("콜라");
            }
            else{
                Lessmoney();
            }
            return;
        }

        // 와인 클릭 확인
        if (wineBounds.contains(mousePosition)) {
            // 와인 아이템 클릭 처리 로직 추가
            if(Game.getMoney() > 1000){
                Game.reduceMoney(1000);
                Game.setRedspd(Game.getRedspd() + 200_000_000);

                Purchase("와인");
            }
            else{
                Lessmoney();
            }
            return;
        }

        // 러버덕 클릭 확인
        if (rubberDuckBounds.contains(mousePosition)) {
            // 러버덕 아이템 클릭 처리 로직 추가
            if(Game.getMoney() > 2000){
                Game.reduceMoney(2000);
                Game.setRubberduckSkill(Game.getRubberduckSkill() + 1);
                Purchase("러버덕");
            }
            else{
                Lessmoney();
            }
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
    private void Purchase(String itemName) {
        purchaseMessage = itemName + "을(를) 구매했습니다!";
        messageDisplayTime = System.nanoTime();
    }
    private void Lessmoney(){
        purchaseMessage = "포인트가 부족합니다!";
        messageDisplayTime = System.nanoTime();
    }
    public void drawPurchaseMessage(Graphics2D g2d) {
        // 메시지 표시 시간이 지나지 않았을 때 메시지를 출력
        if (System.nanoTime() - messageDisplayTime < MESSAGE_DURATION) {
            g2d.setFont(new Font("monospaced", Font.BOLD, 20));
            g2d.setColor(Color.YELLOW);
            g2d.drawString(purchaseMessage, 10, 42); // y값을 21 아래로 설정
        } else {
            // 메시지 표시 시간이 지나면 메시지를 지움
            purchaseMessage = "";
        }
    }
}
