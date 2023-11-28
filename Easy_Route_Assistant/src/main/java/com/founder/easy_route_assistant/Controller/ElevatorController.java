package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.ConvenientDTO;
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

    @GetMapping("/find-elevator/{SGG_NM}")
    public ResponseEntity<List<ConvenientDTO>> findElevator(@PathVariable("SGG_NM") String sgg_nm) {
        List<ConvenientDTO> result = elevatorService.requestElevatorAPI(sgg_nm);
        return ResponseEntity.ok().body(result);
    }
}