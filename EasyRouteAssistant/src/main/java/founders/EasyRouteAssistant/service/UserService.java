package founders.EasyRouteAssistant.service;

import founders.EasyRouteAssistant.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public UserDTO getUserDetail(String id) throws Exception;
}
