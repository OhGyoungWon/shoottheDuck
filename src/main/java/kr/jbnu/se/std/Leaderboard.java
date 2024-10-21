package kr.jbnu.se.std;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class Leaderboard {
    private static DatabaseReference usersRef;
    private static Image leaderboardImage; // 리더보드 배경 이미지
    private static DatabaseReference leaderboardRef;
    private static List<Map.Entry<String, Integer>> leaderboardList;

    public Leaderboard() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        usersRef = db.getReference("users");
        leaderboardRef = db.getReference("users/leaderboard");

        // 리더보드 배경 이미지 로드
        try {
            leaderboardImage = ImageIO.read(new File("src/main/resources/images/LeaderBoard.png")); // 리더보드 배경 이미지 파일 경로
        } catch (IOException e) {
            System.out.println("Failed to load leaderboard background image: " + e.getMessage());
        }
    }

    // 1. 모든 이메일(key)과 스코어(value)를 리스트에 저장하는 메서드
    private static List<Map.Entry<String, Integer>> getLeaderboardData() {
        leaderboardList = new ArrayList<>();
        leaderboardRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot entry : dataSnapshot.getChildren()) {
                    String email = entry.getKey().replace(",", ".");
                    Integer score = entry.getValue(Integer.class);
                    if (score != null) {
                        leaderboardList.add(Map.entry(email, score));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Failed to load leaderboard data: " + databaseError.getMessage());
            }
        });

        System.out.println("저장된 리스트: " + leaderboardList);
        return leaderboardList;
    }

    // 2. 리스트를 스코어가 높은 순서로 정렬하는 메서드
    private static void sortLeaderboardData(List<Map.Entry<String, Integer>> leaderboardList) {
        leaderboardList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
    }

    // 점수를 저장하는 메서드 ('.' → ',')
    public void saveScore(String email, int score) {
        String sanitizedEmail = email.replace(".", ",");

        // Firebase에서 사용자 데이터를 가져옴
        usersRef.child("userInfo/"+sanitizedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 현재 사용자의 topScore를 가져옴
                Integer topScore = dataSnapshot.child("topScore").getValue(Integer.class);

                // topScore와 currentScore 비교
                if (topScore == null || score > topScore) {
                    // currentScore가 topScore보다 크면 topScore를 currentScore로 업데이트
                    Map<String, Object> userUpdates = new HashMap<>();
                    userUpdates.put("userInfo/" + sanitizedEmail + "/topScore", score);
                    userUpdates.put("userInfo/" + sanitizedEmail + "/currentScore", score);
                    userUpdates.put("leaderboard/" + sanitizedEmail, score);


                    usersRef.updateChildrenAsync(userUpdates);
                    System.out.println("Updated topScore to: " + score);
                } else {
                    // currentScore는 그대로 저장
                    Map<String, Object> userUpdates = new HashMap<>();
                    userUpdates.put("userInfo/"+sanitizedEmail + "/currentScore", score);

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

    // 리더보드 배경 이미지를 그리는 정적 메서드
    public static void drawLeaderboardBackground(Graphics2D g, int panelWidth, int panelHeight) {
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
    }

    // 3. 리더보드를 화면에 그리는 메서드
    public static void drawLeaderboard(Graphics2D g, int panelWidth) {
        List<Map.Entry<String, Integer>> leaderboardList = getLeaderboardData();
        System.out.println("Leaderboard Data: " + leaderboardList);

        sortLeaderboardData(leaderboardList);

        // 폰트 및 색상 설정
        g.setFont(new Font("monospaced", Font.BOLD, 20));
        g.setColor(Color.BLACK);

        int y = 150; // 리더보드 점수 출력 시작 위치
        int rank = 1;

        // 상위 10명의 리더보드 점수 출력
        for (int i = 0; i < Math.min(10, leaderboardList.size()); i++) {
            Map.Entry<String, Integer> entry = leaderboardList.get(i);
            String leaderboardEntry = entry.getKey() + " ( " + entry.getValue() + " )";

            // 화면 중앙에 맞추기 위한 x 위치 계산
            int textX = panelWidth / 4;
            int textY = y;

            g.drawString(leaderboardEntry, textX, textY);
            y += 40; // 다음 줄로 이동
        }
    }
}