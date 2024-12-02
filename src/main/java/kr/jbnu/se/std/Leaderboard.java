package kr.jbnu.se.std;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

/**
 * 해당 클래스는 리더보드를 화면에 그리는 매서드
 */

public class Leaderboard {
    private static final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static final Logger log = LoggerFactory.getLogger(Leaderboard.class);
    private static DatabaseReference leaderboardRef = db.getReference("users/leaderboard");
    private static final List<Map.Entry<String, Integer>> leaderboardList = new ArrayList<>();

    private Leaderboard() {
        //...
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
                log.error("Failed to load leaderboard data: {}", databaseError.getMessage());
            }
        });
        return leaderboardList;
    }

    // 2. 리스트를 스코어가 높은 순서로 정렬하는 메서드
    private static void sortLeaderboardData(List<Map.Entry<String, Integer>> leaderboardList) {
        leaderboardList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
    }

    // 3. 리더보드를 화면에 그리는 메서드
    public static void drawLeaderboard(Graphics2D g, int panelWidth, int panelHeight) {
        List<Map.Entry<String, Integer>> leaderboardList = getLeaderboardData();
        sortLeaderboardData(leaderboardList);

        // 폰트 및 색상 설정
        int fontSize = Math.max(9, panelHeight / 23);
        g.setFont(new Font("pixel", Font.BOLD, fontSize));

        g.setColor(Color.BLACK);

        int y = (int) (panelHeight * (360.0 / 720));

        // 상위 10명의 리더보드 점수 출력
        for (int i = 0; i < Math.min(3, leaderboardList.size()); i++) {
            Map.Entry<String, Integer> entry = leaderboardList.get(i);
            String leaderboardEntry = entry.getKey() + "( " + entry.getValue() + " )";

            // 화면 중앙에 맞추기 위한 x 위치 계산
            int textX = (int) (panelWidth /2.0);
            int textY = y;

            g.drawString(leaderboardEntry, textX -90 + (int) (panelWidth * (1.0 / 1280)), textY + fontSize);
            y += fontSize + (int) (panelHeight * (45.0 / 720));
            // 다음 줄로 이동

        }
    }
}
