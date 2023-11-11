package founders.easyRouteAssistant.service;

import founders.easyRouteAssistant.dto.UserDTO;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;


@Service
public interface UserService {

    public void insertUser(UserDTO userDTO) throws ExecutionException, InterruptedException;

    public UserDTO getUserDetail(String id) throws Exception;

    public String deleteUser(String id) throws Exception;
}
