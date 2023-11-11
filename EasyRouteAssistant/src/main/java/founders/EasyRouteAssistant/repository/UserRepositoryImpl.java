package founders.easyRouteAssistant.repository;

import founders.easyRouteAssistant.domain.User;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;


@Repository
public class UserRepositoryImpl implements UserRepository {


    public UserRepositoryImpl() {
    }


    public void save(User user) throws ExecutionException, InterruptedException {

    }
}

