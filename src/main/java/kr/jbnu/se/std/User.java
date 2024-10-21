package kr.jbnu.se.std;

/**
 * Realtime Database에 들어갈 유저 정보를 관리하는 클래스
 * 관리하고 싶은 변수 설정 후 생성자 매개변수에 추가
 * this 포인터 써서 안하면 안 만들어진다
 */

public class User{
    private String password;
    private int currentScore;
    private int topScore;
    private static String nickname;

    public User(String password, int currentScore, int topScore, String nickname) {
        this.password = password;
        this.currentScore = currentScore;
        this.topScore = topScore;
        this.nickname = nickname;
        //...
    }

    public static String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public int getTopScore() {
        return topScore;
    }

    public void setTopScore(int topScore) {
        this.topScore = topScore;
    }
}
