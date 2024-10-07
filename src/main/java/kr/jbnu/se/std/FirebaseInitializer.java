package kr.jbnu.se.std;
// 파이어베이스 서비스 초기화 class

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseInitializer {
    public static void initialize() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("AIzaSyDD_0b35254zoe03RBNYEDODZocbpIklu4");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://your-database-url.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
    }
}
