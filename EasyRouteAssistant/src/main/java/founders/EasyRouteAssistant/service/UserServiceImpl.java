//package founders.easyRouteAssistant.service;
//
//
//import founders.easyRouteAssistant.domain.User;
//import founders.easyRouteAssistant.dto.UserDTO;
//import founders.easyRouteAssistant.repository.UserRepositoryImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.concurrent.ExecutionException;
//
//@Service
//public class UserServiceImpl implements UserService {
//
//    private final UserRepositoryImpl userRepository;
//
//    @Autowired
//    public UserServiceImpl(UserRepositoryImpl userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public void insertUser(UserDTO userDTO) throws ExecutionException, InterruptedException {
//        User user = new User(userDTO.getId(), userDTO.getName(), userDTO.getEmail(), userDTO.getPassword());
//        userRepository.save(user);
//    }
//
////    @Override
////    public void getUserDetail(String id) throws Exception {
//////        DocumentReference documentReference =
//////                userdb.collection(COLLECTION_NAME).document(id);
//////        ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
//////        DocumentSnapshot documentSnapshot = apiFuture.get();
//////        UserRequestDTO user = null;
//////        if (documentSnapshot.exists()) {
//////            user = documentSnapshot.toObject(UserRequestDTO.class);
//////        }
//////        return user;
////    }
//
//
//    @Override
//    public String deleteUser(String id) throws Exception {
//        /*
//            FirestoreClient는 Google Clod Firestore에 접근을 제공
//            이 Api를 사용해서 Firestore 객체(파이어스토어의 쿼리 데이터와 업데이트를 위한 메소드를 제공받을 수 있는)를 얻는다.
//
//            Firestore에 액세스하려면 Google Cloud 프로젝트 ID가 필요합니다.
//            FirestoreClient는 기본 FirebaseApp을 초기화하는 데 사용되는 FirebaseOptions에서 프로젝트 ID를 결정
//
//            만약 사용가능하지 않다면 , 앱을 초기화에 사용되는 자격 증명을 검사
//            결과적으로 이것은 프로젝트 ID를 얻기 위한 시도이다. ( GOOGLE_CLOUD_PROJECT and GCLOUD_PROJECT의 환경번수를 찾는것에 의해)
//
//            만약 프로젝트 아이디를 어떠한 메소드로도 결정할 수 없다면, 이 APi는 런타임에러를 던질 것임.
//
//            https://firebase.google.com/docs/reference/admin/java/reference/com/google/firebase/cloud/FirestoreClient
//
//         */
////        ApiFuture<WriteResult> apiFuture = userdb.collection(COLLECTION_NAME).document(id).delete();
////        return "Document id: " + id + " delete";
//        return null;
//    }
//}
