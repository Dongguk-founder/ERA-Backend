package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.LoginDTO;
import com.founder.easy_route_assistant.DTO.UserDTO;
import com.founder.easy_route_assistant.Service.UserService;
import com.founder.easy_route_assistant.security.Role;
import com.founder.easy_route_assistant.token.JwtProvider;
import com.nimbusds.jose.shaded.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.apache.catalina.User;
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
@RequestMapping(value="/user")
public class UserController {
    private final UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    /*@PostMapping("/check-duplicate") // 매우 수정해야 함.
    public ResponseEntity<Boolean> duplicationCheck(@RequestBody String userID) {
        Boolean dup = userService.duplicationCheck(userID);

        System.out.println(dup);

        return ResponseEntity.status(HttpStatus.OK).body(dup);
    }*/

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<UserDTO> join(@RequestBody UserDTO userDTO) {
        userService.join(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
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
}
