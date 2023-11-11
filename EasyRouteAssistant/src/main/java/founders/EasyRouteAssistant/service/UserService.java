package founders.easyRouteAssistant.service;

import founders.easyRouteAssistant.dto.UserRequestDTO;
import founders.easyRouteAssistant.repository.UserRepository;
import founders.easyRouteAssistant.repository.UserRepositoryImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;


@Service
public interface UserService {

    public void insertUser(UserRequestDTO userDTO) throws ExecutionException, InterruptedException;

    public UserRequestDTO getUserDetail(String id) throws Exception;

    public String deleteUser(String id) throws Exception;
}
