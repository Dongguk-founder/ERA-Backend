package founders.EasyRouteAssistant.controller;

import founders.EasyRouteAssistant.dto.UserDTO;
import founders.EasyRouteAssistant.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    /*

        @Autowired :
        - 의존성 주입을 수행하는 데에 활용
        - 클래스 간의 결합도를 낮추고 유연한 코드를 작성할 수 있도록 도와준다
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getUserDetail/{id}")
    public @ResponseBody ResponseEntity<UserDTO> getUserDetail(@PathVariable String id) throws Exception {

            return ResponseEntity.ok(userService.getUserDetail(id));

    }
}
