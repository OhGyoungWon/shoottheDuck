package kr.jbnu.se.std;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.awt.*;
import java.util.ArrayList;

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
    private static final List<Map.Entry<String, Integer>> leaderboardList = new ArrayList<>();

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
        leaderboardRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot entry : dataSnapshot.getChildren()) {
                    String nickname = entry.getKey();
                    Integer score = entry.getValue(Integer.class);
                    boolean exists = leaderboardList.stream().anyMatch(e -> e.getKey().equals(nickname));
                    if (score != null && !exists) {
                        leaderboardList.add(Map.entry(nickname, score));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Failed to load leaderboard data: " + databaseError.getMessage());
            }
        });
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
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 현재 사용자의 topScore를 가져옴
                Integer topScore = dataSnapshot.child("userInfo/" + sanitizedEmail + "/topScore").getValue(Integer.class);
                String nickname = dataSnapshot.child("userInfo/" + sanitizedEmail + "/nickname").getValue(String.class);

                // topScore와 currentScore 비교
                if (topScore == null || score > topScore) {
                    // currentScore가 topScore보다 크면 topScore를 currentScore로 업데이트
                    Map<String, Object> userUpdates = new HashMap<>();
                    userUpdates.put("userInfo/" + sanitizedEmail + "/topScore", score);
                    userUpdates.put("userInfo/" + sanitizedEmail + "/currentScore", score);
                    userUpdates.put("leaderboard/" + nickname, score);


                    usersRef.updateChildrenAsync(userUpdates);
                    System.out.println("User nickname: " + nickname);
                    System.out.println("Current score: " + score);
                    System.out.println("Updated topScore to: " + score);
                } else {
                    // currentScore는 그대로 저장
                    Map<String, Object> userUpdates = new HashMap<>();
                    userUpdates.put("userInfo/" + sanitizedEmail + "/currentScore", score);

                    usersRef.updateChildrenAsync(userUpdates);
                    System.out.println("User nickname: " + email);
                    System.out.println("Current score saved: " + score);
                }
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
    public static void drawLeaderboard(Graphics2D g, int panelWidth, int panelHeight) {
        List<Map.Entry<String, Integer>> leaderboardList = getLeaderboardData();
        sortLeaderboardData(leaderboardList);

        // 폰트 및 색상 설정
        int fontSize = Math.max(12, panelHeight / 23);
        g.setFont(new Font("monospaced", Font.BOLD, fontSize));

        g.setColor(Color.BLACK);

        int y = 165; // 리더보드 점수 출력 시작 위치

        // 상위 10명의 리더보드 점수 출력
        for (int i = 0; i < Math.min(10, leaderboardList.size()); i++) {
            Map.Entry<String, Integer> entry = leaderboardList.get(i);
            String leaderboardEntry = entry.getKey() + " ( " + entry.getValue() + " )";

            // 화면 중앙에 맞추기 위한 x 위치 계산
            int textX = panelWidth / 3;
            int textY = y;

            g.drawString(leaderboardEntry, textX + 7, textY + fontSize);
            y += fontSize + 13; // 다음 줄로 이동

        }
    }
}
