package kr.jbnu.se.std;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import javax.swing.JOptionPane;

public class FirebaseAuthService {

    public static void register(String email, String password) {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            System.out.println("Successfully created new user: " + userRecord.getUid());
            JOptionPane.showMessageDialog(null, "회원가입 성공!");
        } catch (FirebaseAuthException e) {
            System.err.println("Error creating user: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "회원가입 실패: " + e.getMessage());
        }
    }

    public static void login(String email, String password) {
        try {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            System.out.println("로그인 시도: " + email);
            JOptionPane.showMessageDialog(null, "로그인 성공!");
        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "로그인 실패: " + e.getMessage());
        }
    }
}
