
package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.RouteRequestDTO;
import com.founder.easy_route_assistant.Service.TransferMovementService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/transfer")
public class TransferMovementController {

    private final TransferMovementService transferMovementService;

    @PostMapping("/find")
    public ResponseEntity<JSONObject> findTransferMovement(@RequestBody RouteRequestDTO routeRequestDTO) {
        JSONObject transferMovementDTOList = transferMovementService.findTransferMovement(routeRequestDTO);
        return ResponseEntity.ok().body(transferMovementDTOList);
    }
}

