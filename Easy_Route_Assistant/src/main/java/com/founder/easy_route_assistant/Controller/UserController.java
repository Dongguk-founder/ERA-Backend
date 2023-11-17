package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.LoginDTO;
import com.founder.easy_route_assistant.DTO.UserDTO;
import com.founder.easy_route_assistant.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원가입
    // 회원가입 페이지 출력 요청 - GetMapping으로 출력 요청 -> PostMapping에서 form에 대한 action 수행
    @GetMapping("/join")
    public String saveForm() {
        return "join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute UserDTO userDTO) {
        System.out.println("UserController.save");
        System.out.println("userDTO = " + userDTO);
        String join = userService.join(userDTO);
        System.out.println(join);

        if (join.equals("회원가입 성공")) {
            return "login";
        }
        else return "join";

        // return "home";
    }

    // 로그인
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO loginDTO) {
        System.out.println("LoginDTO = " + loginDTO);
        String login = userService.login(loginDTO);

        System.out.println("jwt = " + login);

        // return "index"; // page mapping 다시
        return "join";
    }
}
