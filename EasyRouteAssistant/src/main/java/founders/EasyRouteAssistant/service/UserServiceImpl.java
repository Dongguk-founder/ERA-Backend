package founders.EasyRouteAssistant.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import founders.EasyRouteAssistant.domain.User;
import founders.EasyRouteAssistant.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final Firestore userdb = FirestoreClient.getFirestore();
    private String COLLECTION_NAME = "Users";

    @Override
    public UserDTO getUserDetail(String id) throws Exception {
        DocumentReference documentReference =
                userdb.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = apiFuture.get();
        UserDTO user = null;
        if (documentSnapshot.exists()) {
            user = documentSnapshot.toObject(UserDTO.class);
        }
        return user;
    }
}
