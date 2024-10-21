package kr.jbnu.se.std;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseConfig {
    private static boolean initialized = false;

    public static void initialize() throws IOException {
        // FirebaseApp이 이미 초기화되었는지 확인
        if (initialized) {
            return; // 초기화가 이미 되어 있다면 메소드 종료
        }
        //만약에 파일 경로로 문제가 생긴다면  Admin SDK key의 파일 절대 경로를 넣을 것
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/stduck-252fa-firebase-adminsdk-896br-4ecff07cb6.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://stduck-252fa-default-rtdb.firebaseio.com")  //오경원 realtime base url
                .build();

        // FirebaseApp이 이미 초기화되어 있는지 확인
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            initialized = true;
            System.out.println("Firebase initialized successfully.");
        } else {
            System.out.println("Firebase is already initialized.");
        }
    }
}
