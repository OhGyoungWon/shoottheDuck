package kr.jbnu.se.std;
import javax.swing.*;

import static kr.jbnu.se.std.FirebaseAuthService.hashPassword;

public class SignUpUI extends JPanel {

    private final JTextField newUsernameField;   // 새로운 사용자 이름 입력 필드
    private final JPasswordField newPasswordField;  // 새로운 비밀번호 입력 필드
    private final JLabel messageLabel;   // 회원가입 결과 메시지 레이블

    private final Window window;  // Window 참조

    // 생성자에서 Window 참조를 받도록 설정
    public SignUpUI(Window window) {
        this.window = window;  // window 객체 저장

        setLayout(null);  // 절대 위치를 사용하여 컴포넌트 배치

        // 사용자 이름 레이블과 텍스트 필드 생성 및 배치
        JLabel usernameLabel = new JLabel("New Email:");
        usernameLabel.setBounds(300, 200, 100, 30);
        add(usernameLabel);

        newUsernameField = new JTextField();
        newUsernameField.setBounds(400, 200, 120, 30);
        add(newUsernameField);

        // 비밀번호 레이블과 비밀번호 필드 생성 및 배치
        JLabel passwordLabel = new JLabel("New Password:");
        passwordLabel.setBounds(300, 250, 100, 30);
        add(passwordLabel);

        newPasswordField = new JPasswordField();
        newPasswordField.setBounds(400, 250, 120, 30);
        add(newPasswordField);

        // 회원가입 버튼 생성 및 배치
        // 회원가입 버튼
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(350, 300, 100, 30);
        add(registerButton);

        // 회원가입 결과를 표시할 레이블
        messageLabel = new JLabel();
        messageLabel.setBounds(300, 350, 250, 30);
        add(messageLabel);

        // 회원가입 버튼에 대한 액션 리스너 추가
        registerButton.addActionListener(e -> {
            handleRegistration();  // 회원가입 처리 메소드 호출
        });
    }

    // 회원가입 처리를 위한 메소드
    private void handleRegistration() {
        String newUsername = newUsernameField.getText();  // 사용자 입력 이름 가져오기
        String newPassword = new String(newPasswordField.getPassword());
        String hashedPassword = hashPassword(newPassword);

        if (!newUsername.isEmpty() && !newPassword.isEmpty()) {
            messageLabel.setText("회원가입 성공!");  // 성공 메시지 표시
            FirebaseAuthService.registerUser(newUsername, newPassword);
            FirebaseAuthService.setUser(newUsername, hashedPassword);

            // 회원가입 성공 후 LoginUI로 돌아가기
            window.switchToLogin();
        } else {
            messageLabel.setText("모든 필드를 입력하세요.");  // 실패 메시지 표시
        }
    }
}
