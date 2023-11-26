package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.ChargerDTO;
import com.founder.easy_route_assistant.Service.ChargerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/charger")
public class ChargerController {

    private final ChargerService chargerService;

    @GetMapping("/get/{target}")
    public ResponseEntity<List<ChargerDTO>> requestChargerAPI(@PathVariable("target") String target) {
        List<ChargerDTO> chargerDTOS = chargerService.requestChargerAPI(target);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(chargerDTOS);
    }
}
