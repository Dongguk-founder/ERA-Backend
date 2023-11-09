package founders.EasyRouteAssistant.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

//로그찍기용 어노테이션
@Slf4j
@Configuration
public class databaseConfig {
    @PostConstruct
    public void  init(){
        try {
            InputStream serviceAccount = new FileInputStream("./src/main/resources/config/key/serviceAccountKey.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            System.out.println();

        }
    }

}
