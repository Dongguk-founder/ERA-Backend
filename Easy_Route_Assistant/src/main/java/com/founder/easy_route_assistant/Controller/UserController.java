package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.LoginDTO;
import com.founder.easy_route_assistant.DTO.UserDTO;
import com.founder.easy_route_assistant.Service.UserService;
import com.founder.easy_route_assistant.security.Role;
import com.founder.easy_route_assistant.token.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<UserDTO> join(@RequestBody UserDTO userDTO) {
        userService.join(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<List<String>> login(@RequestBody LoginDTO loginDTO) {
        List<String> res = new ArrayList<>();

        String jwt = userService.login(loginDTO);
//        String role = jwtProvider.getRole(jwt);

        res.add(jwt);
//        res.add(role);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
    }
}
