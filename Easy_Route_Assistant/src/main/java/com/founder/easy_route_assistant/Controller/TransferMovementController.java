package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.TransferMovementDTO;
import com.founder.easy_route_assistant.Service.TransferMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("transfermovement")
public class TransferMovementController {

    private  final TransferMovementService transferMovementService;

    @GetMapping("/find")
    public ResponseEntity<List<TransferMovementDTO>> findTransferMovement(@RequestBody RouteDTO routeDTO){
        List<TransferMovementDTO> transferMovementDTOList = transferMovementService.findTransferMovement(routeDTO);
        return ResponseEntity.ok().body(transferMovementDTOList);
    }
}
