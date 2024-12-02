package kr.jbnu.se.std;

import com.google.firebase.database.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public final class InGameData {

    private static final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static final Logger log = LoggerFactory.getLogger(InGameData.class);
    private static final DatabaseReference usersRef = db.getReference("users/");
    private static int score;

    private InGameData(){
        //not called
    }

    // 점수를 저장하는 메서드 ('.' → ',')
    public static void saveScore(String email, int scoreData) {
        String sanitizedEmail = email.replace(".", ",");
        score = scoreData;

        // Firebase에서 사용자 데이터를 가져옴
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 현재 사용자의 topScore를 가져옴
                Integer topScore = dataSnapshot.child("userInfo/" + sanitizedEmail + "/topScore").getValue(Integer.class);
                String nickname = dataSnapshot.child("userInfo/" + sanitizedEmail + "/nickname").getValue(String.class);
                Map<String, Object> userUpdates = new HashMap<>();

                // topScore와 currentScore 비교
                if (topScore == null || score > topScore) {
                    // currentScore가 topScore보다 크면 topScore를 currentScore로 업데이트
                    userUpdates.put("userInfo/" + sanitizedEmail + "/topScore", score);
                    userUpdates.put("userInfo/" + sanitizedEmail + "/currentScore", score);
                    userUpdates.put("leaderboard/" + nickname, score);

                    usersRef.updateChildrenAsync(userUpdates);
                } else {
                    // currentScore는 그대로 저장
                    userUpdates.put("userInfo/" + sanitizedEmail + "/currentScore", score);

                    usersRef.updateChildrenAsync(userUpdates);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                log.info("Failed to read user data: {}", databaseError.getMessage());
            }
        });
    }
}
