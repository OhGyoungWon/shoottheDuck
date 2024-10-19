package kr.jbnu.se.std;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Leaderboard {
    private final DatabaseReference usersRef;
    private Image leaderboardImage;

    public Leaderboard() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        this.usersRef = db.getReference("Users");

        // Leaderboard 이미지 로드
        try {
            leaderboardImage = ImageIO.read(new File("src/main/resources/images/LeaderBoard.png"));
        } catch (IOException e) {
            System.out.println("Failed to load leaderboard image: " + e.getMessage());
        }
    }

    // 점수를 저장하는 메서드 ('.' → ',')
    public void saveScore(String email, int score) {
        String sanitizedEmail = email.replace(".", ",");
        usersRef.child(sanitizedEmail).child("score").setValueAsync(score);
    }

    // 리더보드 상위 10개 점수를 가져오는 메서드
    public void getTopScores(ScoreCallback callback) {
        usersRef.orderByChild("score").limitToLast(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError.toException());
            }
        });
    }

    // 리더보드를 화면에 그리는 메서드 (Graphics2D 사용)
    public void renderLeaderboard(Graphics2D g, int panelWidth, int panelHeight) {
        // 배경 이미지를 화면 크기에 맞게 그리기
        if (leaderboardImage != null) {
            // 이미지의 비율을 유지하면서 화면 크기에 맞게 조정
            int imageWidth = leaderboardImage.getWidth(null);
            int imageHeight = leaderboardImage.getHeight(null);

            double widthRatio = (double) panelWidth / imageWidth;
            double heightRatio = (double) panelHeight / imageHeight;
            double scale = Math.min(widthRatio, heightRatio);

            int drawWidth = (int) (imageWidth * scale);
            int drawHeight = (int) (imageHeight * scale);
            int x = (panelWidth - drawWidth) / 2;
            int y = (panelHeight - drawHeight) / 2;

            g.drawImage(leaderboardImage, x, y, drawWidth, drawHeight, null);
        } else {
            g.drawString("Leaderboard image not found.", 50, 50);
        }

        // Firebase에서 상위 10개 점수를 불러와 화면에 출력
        getTopScores(new ScoreCallback() {
            @Override
            public void onSuccess(DataSnapshot data) {
                int y = 100; // 텍스트 시작 y 좌표
                int rank = 1;

                // 폰트 설정
                g.setFont(new Font("monospaced", Font.BOLD, 20));
                g.setColor(Color.BLACK);

                for (DataSnapshot entry : data.getChildren()) {
                    String userEmail = entry.getKey().replace(",", ".");
                    int userScore = entry.child("score").getValue(Integer.class);

                    // 리더보드 점수 텍스트 출력 (이미지 중앙 부분에 맞추어 표시)
                    String leaderboardEntry = rank + ". " + userEmail + ": " + userScore;
                    int textX = panelWidth / 4;  // 텍스트 x 위치를 화면 크기에 비례하여 조정
                    int textY = y;

                    g.drawString(leaderboardEntry, textX, textY);
                    y += 40; // 다음 줄로 이동
                    rank++;
                }
            }

            @Override
            public void onFailure(Exception e) {
                g.setColor(Color.RED);
                g.drawString("Failed to load leaderboard", 50, 50);
            }
        });
    }

    // 콜백 인터페이스 정의
    public interface ScoreCallback {
        void onSuccess(DataSnapshot data);
        void onFailure(Exception e);
    }
}
