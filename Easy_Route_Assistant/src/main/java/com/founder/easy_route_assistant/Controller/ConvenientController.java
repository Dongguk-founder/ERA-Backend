package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.Convenient.ConvenientListDTO;
import com.founder.easy_route_assistant.Service.Convenient.ConvenientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ConvenientListDTO> getConvenientList(@PathVariable String convenientType) {
        // List<ConvenientDTO> convenientDTOList = convenientService.getConvenientList(convenientType);
        ConvenientListDTO convenientDTOList = convenientService.getConvenientList(convenientType);

        if (!convenientDTOList.getConvenientDTOList().isEmpty()){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(convenientDTOList);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
