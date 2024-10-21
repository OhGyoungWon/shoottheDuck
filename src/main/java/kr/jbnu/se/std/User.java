package kr.jbnu.se.std;

import com.google.firebase.database.*;

/**
 * Realtime Database에 들어갈 유저 정보를 관리하는 클래스
 * 관리하고 싶은 변수 설정 후 생성자 매개변수에 추가
 * this 포인터 써서 안하면 안 만들어진다
 */
public class User {
    private static final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static final DatabaseReference usersRef = db.getReference("users/userInfo");
    public String password;
    public int currentScore;
    public int topScore;
    public String nickname;

    public User(String password, int currentScore, int topScore, String nickname) {
        this.password = password;
        this.currentScore = currentScore;
        this.topScore = topScore;
        this.nickname = nickname;
        //...
    }
}