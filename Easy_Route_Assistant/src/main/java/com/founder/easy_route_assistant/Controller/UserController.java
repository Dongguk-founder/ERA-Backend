package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.LoginDTO;
import com.founder.easy_route_assistant.DTO.UserDTO;
import com.founder.easy_route_assistant.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<UserDTO> join(@RequestBody UserDTO userDTO) {
        userService.join(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        String jwt = userService.login(loginDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(jwt);
    }
}
