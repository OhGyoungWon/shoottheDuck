package kr.jbnu.se.std;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Item {
    private String name;
    private int price;
    private BufferedImage image;
    private String description;  // 아이템 설명 추가

    public Item(String name, int price, BufferedImage image, String description) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;  // 설명 초기화
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    // 문자열을 주어진 너비에 맞춰서 여러 줄로 나누는 함수
    public ArrayList<String> wrapText(String text, int lineWidth, FontMetrics metrics) {
        ArrayList<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (String word : text.split(" ")) {
            // 현재 줄과 다음 단어를 합쳤을 때의 너비를 계산
            int lineWidthWithWord = metrics.stringWidth(currentLine + " " + word);

            // 너비를 넘으면 현재 줄을 추가하고 새 줄 시작
            if (lineWidthWithWord > lineWidth) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            } else {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            }
        }

        // 마지막 줄 추가
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    // 아이템을 그리는 함수
    public void drawItem(Graphics2D g2d, int x, int y) {
        g2d.drawImage(image, x, y, null);
        g2d.setColor(Color.WHITE);
        g2d.drawString(name + ": " + price + " Points", x, y + image.getHeight() + 30);
    }

    // 아이템 설명을 그리는 함수
    public void drawDescription(Graphics2D g2d, int x, int y, int maxWidth) {
        g2d.setColor(Color.WHITE);
        FontMetrics metrics = g2d.getFontMetrics();

        // 설명을 주어진 너비에 맞게 여러 줄로 나눔
        ArrayList<String> lines = wrapText(description, maxWidth, metrics);

        // 각 줄을 순차적으로 그리기
        int lineHeight = metrics.getHeight();
        for (int i = 0; i < lines.size(); i++) {
            g2d.drawString(lines.get(i), x, y + (i * lineHeight));
        }
    }

    // 아이템을 클릭했는지 확인하는 함수
    public boolean isClicked(Point mousePosition, int x, int y) {
        Rectangle itemBounds = new Rectangle(x, y, image.getWidth(), image.getHeight());
        return itemBounds.contains(mousePosition);
    }
}
