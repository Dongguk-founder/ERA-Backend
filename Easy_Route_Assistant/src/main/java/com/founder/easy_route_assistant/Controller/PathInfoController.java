package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.PathInfoDTO;
import com.founder.easy_route_assistant.DTO.PathRequestDTO;
import com.founder.easy_route_assistant.Service.PathInfoService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("pathInfo")
public class PathInfoController {

    private  final PathInfoService transferMovementService;

    @GetMapping("/find")
    public ResponseEntity<JSONObject> findTransferMovement(@RequestBody PathRequestDTO pathRequestDTO){
        JSONObject pathInfoDTOList = transferMovementService.findPathInfo(pathRequestDTO);
        return ResponseEntity.ok().body(pathInfoDTOList);
    }
}
