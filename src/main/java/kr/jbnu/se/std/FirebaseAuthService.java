package kr.jbnu.se.std;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.database.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FirebaseAuthService {

    public FirebaseAuthService() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = db.getReference("users/userInfo");
        DatabaseReference leaderboardRef = db.getReference("users/leaderboard");
    }

    // Firebase AuthService에 사용자를 등록하는 메소드
    public static void registerUser(String username, String password) {
        CreateRequest request = new CreateRequest();
        request.setEmail(username);
        request.setPassword(password);

        try {
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request); //firebase에 사용자 등록
            System.out.println("Successfully created new user: " + userRecord.getUid());
        } catch (Exception e) {
            System.out.println("Error creating new user: " + e.getMessage());
        }
    }

    //Firebase Realtime Database에 사용자 정보를 저장하는 메소드
    public static void setUser(String email, String password, String nickname) {
        // Firebase Realtime Database 참조 가져오기(User라는 큰 데이터 틀 형성)
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users/userInfo");

        // 이메일을 키로 사용하기 위해 특수문자 "."를 ","로 변환
        String sanitizedEmail = email.replace(".", ",");

        // 이메일 아래에 비밀번호 저장
        databaseRef.child(sanitizedEmail).setValue(new User(password, 0, 0, nickname), (databaseError, databaseReference) -> {
            if (databaseError != null) {
                System.out.println("Error saving user: " + databaseError.getMessage());
            } else {
                System.out.println("User saved successfully.");
            }
        });
    }

    // 이메일과 비밀번호로 로그인하는 메서드
    public static boolean loginUser(String email, String password) {
        // 이메일을 키로 사용하기 위해 특수문자 "."를 ","로 변환
        String sanitizedEmail = email.replace(".", ",");

        // Firebase Realtime Database 참조 가져오기
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users/userInfo/" + sanitizedEmail);

        // 비동기 결과를 동기적으로 처리하기 위해 CompletableFuture 사용
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // 비밀번호를 동일한 방식으로 해시
        String hashedPassword = hashPassword(password);

        // 데이터베이스에서 비밀번호 가져오기
        databaseRef.child("/password").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String storedHashedPassword = dataSnapshot.getValue(String.class);

                if (storedHashedPassword != null && storedHashedPassword.equals(hashedPassword)) {
                    System.out.println("User logged in successfully.");
                    future.complete(true);  // 비밀번호가 일치하면 true 반환
                } else {
                    System.out.println("Incorrect password.");
                    future.complete(false);  // 비밀번호가 일치하지 않으면 false 반환
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Database error: " + databaseError.getMessage());
                future.complete(false);  // 데이터베이스 오류 시 false 반환
            }
        });

        try {
            return future.get();  // 비동기 작업이 완료될 때까지 대기하고 결과 반환
        } catch (InterruptedException | ExecutionException e) {
            return false;  // 예외 발생 시 false 반환
        }
    }

    // 비밀번호 해싱 함수 예시
    static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}


