package kr.jbnu.se.std;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

public class LoginUI extends JPanel {

    private static JTextField emailField;   // 사용자 이름 입력 필드
    private static JPasswordField passwordField;  // 비밀번호 입력 필드
    private JButton loginButton;   // 로그인 버튼
    private JLabel messageLabel;   // 로그인 결과 메시지 레이블
    private JButton signUpButton;
    private Window window;

    private static String userEmail;

    public LoginUI(Window window) {
        this.window = window;

        // GridBagLayout을 사용하여 화면 중앙에 컴포넌트 배치
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // 컴포넌트 간의 간격 설정

        // 사용자 이름 레이블과 텍스트 필드 생성 및 배치
        JLabel usernameLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(usernameLabel, gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(emailField, gbc);

        // 비밀번호 레이블과 비밀번호 필드 생성 및 배치
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(passwordField, gbc);

        // 로그인 버튼 생성 및 배치
        loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(loginButton, gbc);

        // 회원가입 버튼 생성 및 배치
        signUpButton = new JButton("Sign Up");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(signUpButton, gbc);

        // 로그인 결과를 표시할 레이블
        messageLabel = new JLabel();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        add(messageLabel, gbc);

        // 로그인 버튼에 대한 액션 리스너 추가
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    handleLogin();  // 로그인 처리 메소드 호출
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // 회원가입 버튼에 대한 액션 리스너 추가
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.switchToSignUp();  // 회원가입 화면으로 전환하는 메소드 호출
            }
        });
    }

    public static String getuserEmail() {
        userEmail = emailField.getText();
        return userEmail;
    }

    // 로그인 처리를 위한 메소드
    private void handleLogin() throws IOException {
        String email = emailField.getText();  // 사용자 입력 이름 가져오기
        String password = new String(passwordField.getPassword());  // 비밀번호 가져오기
        // 사용자 이름과 비밀번호가 유효한지 확인
        if (FirebaseAuthService.runloginUser(email, password)) {
            messageLabel.setText("로그인 성공!");  // 성공 메시지 표시
            // 로그인 성공 시 Window의 콘텐츠를 Framework로 전환
            window.switchToFramework();
        } else {
            messageLabel.setText("유효하지 않은 이메일 또는 비밀번호.");  // 실패 메시지 표시
        }
    }
}
