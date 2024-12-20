package kr.jbnu.se.std;
import javax.swing.*;

import java.awt.*;

import static kr.jbnu.se.std.FirebaseAuthService.hashPassword;

public class SignUpUI extends JPanel {

    private final JTextField newEmailField;   // 새로운 사용자 이름 입력 필드
    private final JPasswordField newPasswordField;  // 새로운 비밀번호 입력 필드
    private final JTextField newNicknameField;
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
        JLabel userNicknameLabel = new JLabel("New Nickname:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(userNicknameLabel, gbc);

        newNicknameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(newNicknameField, gbc);

        JLabel emailLabel = new JLabel("New Email:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(emailLabel, gbc);

        newEmailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(newEmailField, gbc);

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

        // 로그인 화면으로 돌아가는 BACK 버튼 추가
        JButton backButton = new JButton("BACK");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(backButton, gbc);

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

        // 돌아가기 버튼에 대한 액션 리스너 추가
        backButton.addActionListener(e -> window.switchToLogin());
    }

    // 회원가입 처리를 위한 메소드
    private void handleRegistration() {
        String newEmail = newEmailField.getText();  // 사용자 입력 이름 가져오기
        String newNickname = newNicknameField.getText();
        String hashedPassword = hashPassword(new String(newPasswordField.getPassword()));

        if (!newEmail.isEmpty() && !hashedPassword.isEmpty()) {
            messageLabel.setText("회원가입 성공!");  // 성공 메시지 표시
            FirebaseAuthService.runRegisterUser(newEmail, hashedPassword, newNickname);

            // 회원가입 성공 후 LoginUI로 돌아가기
            window.switchToLogin();
        } else {
            messageLabel.setText("모든 필드를 입력하세요.");  // 실패 메시지 표시
        }
    }
}
