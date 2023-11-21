package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.ConvenientDTO;
import com.founder.easy_route_assistant.Entity.RequestEntity;
import com.founder.easy_route_assistant.Service.ConvenientService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ConvenientController {
    private final ConvenientService convenientService;

    @PostMapping("/add-convenient")
    public ResponseEntity<ConvenientDTO> createConvenient(@RequestBody ConvenientDTO convenientDTO) {
        convenientService.save(convenientDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(convenientDTO);
    }

    @GetMapping("/convenient-list")
    public ResponseEntity<List<ConvenientDTO>> getConvenientList() {
        List<ConvenientDTO> convenientDTOS = convenientService.getConvenientList();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(convenientDTOS);
    }
}