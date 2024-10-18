package kr.jbnu.se.std;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class LoginUI extends JPanel {


    private JTextField usernameField;   // 사용자 이름 입력 필드
    private JPasswordField passwordField;  // 비밀번호 입력 필드
    private JButton loginButton;   // 로그인 버튼
    private JLabel messageLabel;   // 로그인 결과 메시지 레이블
    private JButton signUpButton;
    private Window window;

    public LoginUI(Window window) {
        this.window = window;

        setLayout(null);  // 절대 위치를 사용하여 컴포넌트 배치

        // 사용자 이름 레이블과 텍스트 필드 생성 및 배치
        JLabel usernameLabel = new JLabel("Email:");
        usernameLabel.setBounds(300, 200, 80, 30);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(380, 200, 120, 30);
        add(usernameField);

        // 비밀번호 레이블과 비밀번호 필드 생성 및 배치
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(300, 250, 80, 30);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(380, 250, 120, 30);
        add(passwordField);

        // 로그인 버튼 생성 및 배치
        loginButton = new JButton("Login");
        loginButton.setBounds(295, 300, 100, 30);
        add(loginButton);

        //회원가입 버튼 생성 및 배치
        signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(400, 300, 100, 30);
        add(signUpButton);

        // 로그인 결과를 표시할 레이블
        messageLabel = new JLabel();
        messageLabel.setBounds(300, 350, 200, 30);
        add(messageLabel);

        // 로그인 버튼에 대한 액션 리스너 추가
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();  // 로그인 처리 메소드 호출
            }
        });

        //회원가입 버튼에 대한 액션 리스너 추가
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.switchToSignUp();  //회원가입 화면으로 전환하는 메소드 호출
            }
        });
    }

    // 로그인 처리를 위한 메소드
    private void handleLogin() {
        String username = usernameField.getText();  // 사용자 입력 이름 가져오기
        String password = new String(passwordField.getPassword());  // 비밀번호 가져오기

        // 사용자 이름과 비밀번호가 유효한지 확인
        if (FirebaseAuthService.loginUser(username, password)) {
            messageLabel.setText("로그인 성공!");  // 성공 메시지 표시
            // 로그인 성공 시 Window의 콘텐츠를 Framework로 전환
            window.switchToFramework();
        } else {
            messageLabel.setText("유효하지 않은 이메일 또는 비밀번호.");  // 실패 메시지 표시
        }
    }
}
