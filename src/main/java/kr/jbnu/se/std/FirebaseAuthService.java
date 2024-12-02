package kr.jbnu.se.std;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.database.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FirebaseAuthService {

    private static final Logger log = LoggerFactory.getLogger(FirebaseAuthService.class);
    static FirebaseDatabase db = FirebaseDatabase.getInstance();
    static DatabaseReference usersRef = db.getReference("users/userInfo");

    private FirebaseAuthService() {
        //...
    }

    // Firebase AuthService에 사용자를 등록하는 메소드
    private static void registerUser(String email, String password) {
        CreateRequest request = new CreateRequest();
        request.setEmail(email);
        request.setPassword(password);

        try {
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request); //firebase에 사용자 등록
            log.info("Successfully created new user: {}", userRecord.getUid());
        } catch (Exception e) {
            log.info("Error creating new user: {}", e.getMessage());
        }
    }

    //Firebase Realtime Database에 사용자 정보를 저장하는 메소드
    private static void setUser(String email, String password, String nickname) {
        String sanitizedEmail = email.replace(".", ",");

        // 이메일 아래에 비밀번호 저장
        usersRef.child(sanitizedEmail).setValue(new User(password, 0, 0, nickname),
                (databaseError, databaseReference) -> {
            if (databaseError != null)
                log.info("Error saving user: {}", databaseError.getMessage());
            else {
                log.info("User saved successfully.");
            }
        });
    }

    public static void runRegisterUser(String email, String password, String nickname){
        registerUser(email, password);
        setUser(email, password, nickname);
    }

    // 이메일과 비밀번호로 로그인하는 메서드
    public static boolean runLoginUser(String email, String password) throws InterruptedException, ExecutionException {
        String sanitizedEmail = email.replace(".", ",");
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users/userInfo/" + sanitizedEmail);
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        String hashedPassword = hashPassword(password);

        // 데이터베이스에서 비밀번호 가져오기
        databaseRef.child("/password").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String storedHashedPassword = dataSnapshot.getValue(String.class);

                if (storedHashedPassword != null && storedHashedPassword.equals(hashedPassword)) {
                    log.info("User logged in successfully.");
                    future.complete(true);  // 비밀번호가 일치하면 true 반환
                } else {
                    log.info("Incorrect password.");
                    future.complete(false);  // 비밀번호가 일치하지 않으면 false 반환
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                log.info("Database error: {}", databaseError.getMessage());
                future.complete(false);  // 데이터베이스 오류 시 false 반환
            }
        });

        return future.get();
    }

    // 비밀번호 해싱 함수
    public static String hashPassword(String password) {
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


