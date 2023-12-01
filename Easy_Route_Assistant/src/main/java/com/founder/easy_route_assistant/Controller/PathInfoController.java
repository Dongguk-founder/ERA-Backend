package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.RouteRequestDTO;
import com.founder.easy_route_assistant.Service.PathInfoService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/path")
public class PathInfoController {

    private final PathInfoService pathInfoService;

    @GetMapping("/find")
    public ResponseEntity<JSONObject> getPathInfo(@ResponseBody RouteRequestDTO routeRequestDTO){

    }
}
