package founders.easyRouteAssistant.service;


import founders.easyRouteAssistant.domain.User;
import founders.easyRouteAssistant.dto.UserDTO;
import founders.easyRouteAssistant.repository.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepositoryImpl userRepository;

    @Override
    public void insertUser(UserDTO userDTO) throws ExecutionException, InterruptedException {
        User user = new User(userDTO.getId(), userDTO.getName(), userDTO.getEmail(), userDTO.getPassword());
        userRepository.save(user);
    }

    @Override
    public UserDTO getUserDetail(String id) throws Exception {
        return null;
    }

    @Override
    public String deleteUser(String id) throws Exception {
        return null;
    }
}
