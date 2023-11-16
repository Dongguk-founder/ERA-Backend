package founders.easyRouteAssistant.controller;

import founders.easyRouteAssistant.dto.UserDTO;
import founders.easyRouteAssistant.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//"/users/register"로 들어오는 API주소를 mapping
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;



    /*
        @Autowired :
        - 의존성 주입을 수행하는 데에 활용
        - 클래스 간의 결합도를 낮추고 유연한 코드를 작성할 수 있도록 도와준다
     */

    @PostMapping("/register")
    public @ResponseBody ResponseEntity<String> registerUser(UserDTO userDTO) throws Exception {
        userService.insertUser(userDTO);
        return ResponseEntity.ok("User registered successfully");
    }

    /*
        @GetMapping :
        - http get 요청에 대한 핸들러 메서드를 지정하는데 사용된다
        - 특정 url에 대한 get요청을 처리할 수 있는 메서드를 간단하게 정의 할 수 있다.

        @PathVariable :
        - url 패턴에서 변수 값을 추출하는 데 사용되는 어노테이션
        - url에서 {…}이 안에 있는 값을 추출해서 사용하는 방식
     */
    @GetMapping("/getUserDetail/{id}")
    public UserDTO getUserDetail(@PathVariable String id) throws Exception {
        return userService.getUserDetail(id);
    }
//    public UserRequestDTO getUserDetail(@RequestParam String id) throws Exception {
//
//        return UserService.getUserDetail(id);
//}
}

