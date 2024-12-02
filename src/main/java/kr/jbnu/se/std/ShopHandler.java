package kr.jbnu.se.std;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ShopHandler {

    private boolean isVisible = true;

    private Rectangle closeButtonBounds;
    private Rectangle glovesBounds;
    private Rectangle bulletBounds;
    private Rectangle colaBounds;
    private Rectangle wineBounds;
    private Rectangle rubberDuckBounds;
    private static String purchaseMessage = "";  // 구매 메시지 저장 변수
    private static long messageDisplayTime = 0;  // 메시지 표시 시간

    public static String getPurchaseMessage() {
        return purchaseMessage;
    }

    public static void setPurchaseMessage(String purchaseMessage) {
        ShopHandler.purchaseMessage = purchaseMessage;
    }

    public static long getMessageDisplayTime() {
        return messageDisplayTime;
    }

    public void setMessageDisplayTime(long messageDisplayTime) {
        ShopHandler.messageDisplayTime = messageDisplayTime;
    }

    // 아이템 정보를 구조화하여 관리
    private class ShopItem {
        int cost;
        Consumer<Void> onPurchase;

        public ShopItem(int cost, Consumer<Void> onPurchase) {
            this.cost = cost;
            this.onPurchase = onPurchase;
        }
    }

    private final Map<Rectangle, ShopItem> shopItems = new HashMap<>();

    public ShopHandler() {
        // 아이템별 클릭 영역과 동작 매핑
        shopItems.put(glovesBounds, new ShopItem(200, v -> {
            Game.setMoney(Game.getMoney() - 200);
            Game.getlvdata();
            Purchase("장갑");
        }));

        shopItems.put(bulletBounds, new ShopItem(300, v -> {
            Game.setMoney(Game.getMoney() - 300);
            if (Game.getReloadDuration() <= 300_000_000) {
                Game.setReloadDuration(300_000_000);
            } else {
                Game.setReloadDuration(Game.getReloadDuration() - 300_000_000);
            }
            Purchase("총알");
        }));

        shopItems.put(colaBounds, new ShopItem(500, v -> {
            Game.setMoney(Game.getMoney() - 500);
            if (Game.getRunawayDucks() < 20) {
                Game.setRunawayDucks(0);
            } else {
                Game.setRunawayDucks(Game.getRunawayDucks() - 20);
            }
            Purchase("콜라");
        }));

        shopItems.put(wineBounds, new ShopItem(1000, v -> {
            Game.setMoney(Game.getMoney() - 1000);
            Game.setReduceSpeed(Game.getReduceSpeed() + 200_000_000);
            Purchase("와인");
        }));

        shopItems.put(rubberDuckBounds, new ShopItem(2000, v -> {
            Game.setMoney(Game.getMoney() - 2000);
            Game.setRubberduckKills(Game.getRubberducksKills() + 1);
            Purchase("러버덕");
        }));
    }

    public void handleClick(Point mousePosition) {
        if (!isVisible) return;

        // 닫기 버튼 처리
        if (closeButtonBounds.contains(mousePosition)) {
            closeShop();
            return;
        }

        // 상점 아이템 클릭 처리
        for (Map.Entry<Rectangle, ShopItem> entry : shopItems.entrySet()) {
            if (entry.getKey().contains(mousePosition)) {
                ShopItem item = entry.getValue();
                if (Game.getMoney() >= item.cost) {
                    item.onPurchase.accept(null);
                } else {
                    LessMoney();
                }
                return;
            }
        }
    }

    public void closeShop() { isVisible = false; }

    private void Purchase(String itemName) {
        purchaseMessage = itemName + "을(를) 구매했습니다!";
        messageDisplayTime = System.nanoTime();
        closeShop();  // 상점 닫기
    }
    private void LessMoney(){
        purchaseMessage = "포인트가 부족합니다!";
        messageDisplayTime = System.nanoTime();
    }
}
