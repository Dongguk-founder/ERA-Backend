package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.RouteRequestDTO;
import com.founder.easy_route_assistant.Service.PathInfoService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/path")
public class PathInfoController {

    private final PathInfoService pathInfoService;

    @GetMapping("/find")
    public ResponseEntity<JSONObject> getPathInfo(@RequestBody RouteRequestDTO routeRequestDTO){
         JSONObject jsonObject = pathInfoService.requestLocationAPI(routeRequestDTO);
         return ResponseEntity.ok().body(jsonObject);
    }
}
