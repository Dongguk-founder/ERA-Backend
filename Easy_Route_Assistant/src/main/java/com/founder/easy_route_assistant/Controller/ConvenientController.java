package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.ConvenientDTO;
import com.founder.easy_route_assistant.Service.ConvenientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value="/convenient")
public class ConvenientController {

    private final ConvenientService convenientService;

    /*@PostMapping("/add")
    public ResponseEntity<ConvenientDTO> createConvenient(@RequestBody ConvenientDTO convenientDTO) {
        convenientService.update(convenientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(convenientDTO);
    }*/

    @GetMapping("/get-{convenientType}")
    public ResponseEntity<List<ConvenientDTO>> getConvenientList(@PathVariable String convenientType) {
        List<ConvenientDTO> convenientDTOList = convenientService.getConvenientList(convenientType);
        if (!convenientDTOList.isEmpty()){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(convenientDTOList);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
