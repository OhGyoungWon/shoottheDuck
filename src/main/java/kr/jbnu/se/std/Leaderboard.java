package kr.jbnu.se.std;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class Leaderboard {
    private final DatabaseReference usersRef;
    private Image leaderboardImage; // 리더보드 배경 이미지
    private Image savedScoreImage;  // 저장된 점수 배경 이미지

    public Leaderboard() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        this.usersRef = db.getReference("users");

        // 리더보드 배경 이미지 로드
        try {
            leaderboardImage = ImageIO.read(new File("src/main/resources/images/LeaderBoard.png")); // 리더보드 배경 이미지 파일 경로
        } catch (IOException e) {
            System.out.println("Failed to load leaderboard background image: " + e.getMessage());
        }

        // 저장된 점수 배경 이미지 로드
        try {
            savedScoreImage = ImageIO.read(new File("src/main/resources/images/savedscore.png"));
        } catch (IOException e) {
            System.out.println("Failed to load saved score image: " + e.getMessage());
        }
    }

    // 점수를 저장하는 메서드 ('.' → ',')
    public void saveScore(String email, int score) {
        String sanitizedEmail = email.replace(".", ",");

        // Firebase에서 사용자 데이터를 가져옴
        usersRef.child("users").child(sanitizedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 현재 사용자의 topScore를 가져옴
                Integer topScore = dataSnapshot.child("topScore").getValue(Integer.class);

                // topScore와 currentScore 비교
                if (topScore == null || score > topScore) {
                    // currentScore가 topScore보다 크면 topScore를 currentScore로 업데이트
                    Map<String, Object> userUpdates = new HashMap<>();
                    userUpdates.put(sanitizedEmail + "/topScore", score);
                    userUpdates.put(sanitizedEmail + "/currentScore", score);

                    usersRef.updateChildrenAsync(userUpdates);
                    System.out.println("Updated topScore to: " + score);
                } else {
                    // currentScore는 그대로 저장
                    Map<String, Object> userUpdates = new HashMap<>();
                    userUpdates.put(sanitizedEmail + "/currentScore", score);

                    usersRef.updateChildrenAsync(userUpdates);
                    System.out.println("Current score saved: " + score);
                }

                // 로그 출력
                System.out.println("User email: " + email);
                System.out.println("Current score: " + score);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Failed to read user data: " + databaseError.getMessage());
            }
        });
    }

    // 리더보드를 화면에 그리는 메서드 (Graphics2D 사용)
    public void renderLeaderboard(Graphics2D g, int panelWidth, int panelHeight) {
        // 리더보드 배경 이미지를 그리기
        if (leaderboardImage != null) {
            int imageWidth = leaderboardImage.getWidth(null);
            int imageHeight = leaderboardImage.getHeight(null);

            // 리더보드 배경 이미지를 화면 중앙에 맞춰서 그리기
            int leaderboardX = (panelWidth - imageWidth) / 2;
            int leaderboardY = (panelHeight - imageHeight) / 4;

            g.drawImage(leaderboardImage, leaderboardX, leaderboardY, null);
        } else {
            // 리더보드 배경 이미지 로드 실패 시
            g.setFont(new Font("Serif", Font.BOLD, 24));
            g.setColor(Color.RED);
            g.drawString("Failed to load leaderboard background image.", 50, 50);
        }

        // Firebase에서 상위 10개 점수를 불러와 화면에 출력
        getTopScores(new ScoreCallback() {
            @Override
            public void onSuccess(DataSnapshot data) {
                int y = 150;  // 리더보드 점수 출력 시작 위치

                // 폰트 설정
                g.setFont(new Font("monospaced", Font.BOLD, 20));
                g.setColor(Color.BLACK);

                // 리더보드 점수 출력
                for (DataSnapshot entry : data.getChildren()) {
                    String userEmail = entry.getKey().replace(",", ".");
                    Integer userScore = entry.child("score").getValue(Integer.class);

                    if (userScore == null) {
                        continue; // userScore가 null이면 건너뜁니다.
                    }

                    // 리더보드 점수 텍스트 출력
                    String leaderboardEntry = userEmail + " ( " + userScore + " )";
                    int textX = panelWidth / 4;  // 텍스트 x 위치를 화면 크기에 비례하여 조정
                    int textY = y;

                    g.drawString(leaderboardEntry, textX, textY);
                    y += 40; // 다음 줄로 이동
                }
            }

            @Override
            public void onFailure(Exception e) {
                g.setColor(Color.RED);
                g.drawString("Failed to load leaderboard", 50, 50);
            }
        });
    }

    // 게임 오버 시 저장된 점수를 화면에 그리는 메서드
    public void renderSavedScore(Graphics2D g, int panelWidth, int panelHeight, int savedScore) {
        // 저장된 점수 배경 이미지를 그리기
        if (savedScoreImage != null) {
            int imageWidth = savedScoreImage.getWidth(null);
            int imageHeight = savedScoreImage.getHeight(null);

            // 이미지의 위치와 크기를 화면 크기에 맞추어 조정 (저장된 점수 배경)
            int savedScoreX = panelWidth - imageWidth - 20;  // 오른쪽 여백
            int savedScoreY = (panelHeight - imageHeight) / 2;

            g.drawImage(savedScoreImage, savedScoreX, savedScoreY, null);

            // 저장된 점수 텍스트 표시
            g.setFont(new Font("monospace", Font.BOLD, 24));
            g.setColor(Color.BLACK);
            String scoreText = "저장된 점수: " + savedScore;

            FontMetrics metrics = g.getFontMetrics(g.getFont());
            int textX = savedScoreX + (imageWidth - metrics.stringWidth(scoreText)) / 2;
            int textY = savedScoreY + imageHeight / 2;

            g.drawString(scoreText, textX, textY);
        } else {
            // 저장된 점수 배경 이미지 로드 실패 시
            g.setFont(new Font("monospace", Font.BOLD, 24));
            g.setColor(Color.RED);
            g.drawString("Failed to load saved score image.", 50, 50);
        }
    }

    // Firebase로부터 리더보드 데이터 로드
    private void getTopScores(ScoreCallback callback) {
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

    // 콜백 인터페이스 정의
    public interface ScoreCallback {
        void onSuccess(DataSnapshot data);
        void onFailure(Exception e);
    }
}
