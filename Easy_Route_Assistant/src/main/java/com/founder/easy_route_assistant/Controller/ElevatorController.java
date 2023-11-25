package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.ElevatorDTO;
import com.founder.easy_route_assistant.Service.ElevatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ElevatorController {

    private final ElevatorService elevatorService;

    @GetMapping("/find-elevator/{SW_CD}")
    public ResponseEntity<List<ElevatorDTO>> findElevator(@PathVariable("SW_CD") String sw_cd) {
        List<ElevatorDTO> result = elevatorService.requestElevatorAPI(sw_cd);
        return ResponseEntity.ok().body(result);
    }
}