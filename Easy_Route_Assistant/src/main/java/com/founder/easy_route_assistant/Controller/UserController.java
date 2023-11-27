package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.LoginDTO;
import com.founder.easy_route_assistant.DTO.UserDTO;
import com.founder.easy_route_assistant.Service.UserService;
import com.founder.easy_route_assistant.config.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@RequiredArgsConstructor
@RequestMapping(value="/user")
public class UserController {
    private final UserService userService;


    private final JwtProvider jwtProvider;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<UserDTO> join(@RequestBody UserDTO userDTO) {
        userDTO = userService.join(userDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userDTO);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<JSONObject> login(@RequestBody LoginDTO loginDTO) {
        JSONObject res = new JSONObject();

        String jwt = userService.login(loginDTO);
        String role = jwtProvider.getRole(jwt);

        res.put("jwt", jwt);
        res.put("role", role);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
    }

    // 로그아웃
    @PostMapping("/logout")
    public HttpStatus logout(@RequestHeader String jwt) {
        userService.logout(jwt);

        return HttpStatus.OK;
    }
}
