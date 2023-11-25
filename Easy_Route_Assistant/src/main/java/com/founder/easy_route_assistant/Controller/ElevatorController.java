package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.Service.ElevatorService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ElevatorController {

    private final ElevatorService elevatorService;

    @GetMapping("/find-elevator")
    public ResponseEntity<JSONArray> findElevator(String subway) {
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        JSONArray result = elevatorService.requestElevatorAPI();
        return ResponseEntity.ok().body(result);
    }
}