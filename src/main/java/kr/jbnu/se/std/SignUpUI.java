package kr.jbnu.se.std;
import javax.swing.*;

import java.awt.*;

import static kr.jbnu.se.std.FirebaseAuthService.hashPassword;

public class SignUpUI extends JPanel {

    private final JTextField newUsernameField;   // 새로운 사용자 이름 입력 필드
    private final JPasswordField newPasswordField;  // 새로운 비밀번호 입력 필드
    private final JTextField newNiknameField;
    private final JLabel messageLabel;   // 회원가입 결과 메시지 레이블

    private final Window window;  // Window 참조

    // 생성자에서 Window 참조를 받도록 설정
    public SignUpUI(Window window) {
        this.window = window;  // window 객체 저장

        // GridBagLayout을 사용하여 화면 중앙에 컴포넌트 배치
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // 컴포넌트 간의 간격 설정

        // 사용자 이름 레이블과 텍스트 필드 생성 및 배치
        JLabel userNiknameLabel = new JLabel("New Nikname:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(userNiknameLabel, gbc);

        newNiknameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(newNiknameField, gbc);

        JLabel usernameLabel = new JLabel("New Email:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(usernameLabel, gbc);

        newUsernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(newUsernameField, gbc);

        // 비밀번호 레이블과 비밀번호 필드 생성 및 배치
        JLabel passwordLabel = new JLabel("New Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(passwordLabel, gbc);

        newPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(newPasswordField, gbc);

        // 회원가입 버튼 생성 및 배치
        // 회원가입 버튼
        JButton registerButton = new JButton("Register");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(registerButton, gbc);

        // 회원가입 결과를 표시할 레이블
        messageLabel = new JLabel();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        add(messageLabel, gbc);

        // 회원가입 버튼에 대한 액션 리스너 추가
        registerButton.addActionListener(e -> {
            handleRegistration();  // 회원가입 처리 메소드 호출
        });
    }

    // 회원가입 처리를 위한 메소드
    private void handleRegistration() {
        String newUsername = newUsernameField.getText();  // 사용자 입력 이름 가져오기
        String newPassword = new String(newPasswordField.getPassword());
        String newNikname = newNiknameField.getText();
        String hashedPassword = hashPassword(newPassword);

        if (!newUsername.isEmpty() && !newPassword.isEmpty()) {
            messageLabel.setText("회원가입 성공!");  // 성공 메시지 표시
            FirebaseAuthService.registerUser(newUsername, newPassword);
            FirebaseAuthService.setUser(newUsername, hashedPassword, newNikname);

            // 회원가입 성공 후 LoginUI로 돌아가기
            window.switchToLogin();
        } else {
            messageLabel.setText("모든 필드를 입력하세요.");  // 실패 메시지 표시
        }
    }
}
