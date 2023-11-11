package founders.easyRouteAssistant.repository;


import founders.easyRouteAssistant.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public interface UserRepository {

    public void save(User user) throws ExecutionException, InterruptedException;
}
