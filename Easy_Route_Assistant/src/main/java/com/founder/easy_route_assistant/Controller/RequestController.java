package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.ConvenientDTO;
import com.founder.easy_route_assistant.DTO.RequestDTO;
import com.founder.easy_route_assistant.Service.RequestService;
import com.founder.easy_route_assistant.config.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value="/request")
public class RequestController {

    private final RequestService requestService;
    private final JwtProvider jwtProvider;

    @PostMapping("/send")
    public ResponseEntity<RequestDTO> createRequest(@RequestHeader String jwt, @RequestBody RequestDTO requestDTO) {
        requestService.createRequest(jwt, requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/get")
    public ResponseEntity<List<RequestDTO>> requestDTOS(@RequestHeader String jwt) {
        List<RequestDTO> requestDTOList = requestService.getAllRequests(jwt);
        if(!requestDTOList.isEmpty()){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(requestDTOList);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    /*@PatchMapping("/update")
    public ResponseEntity<RequestDTO> requestAccept(@RequestHeader String jwt, @RequestBody RequestDTO requestDTO) {
        HttpStatus status = requestService.updateRequest(jwt, requestDTO);

        return ResponseEntity.status(status).build();
    }*/

    @PatchMapping("/update/{id}/{accepted}")
    public ResponseEntity<ConvenientDTO> requestAccept(@PathVariable int id, @PathVariable boolean accepted, @RequestHeader String jwt, @RequestBody ConvenientDTO convenientDTO) {
        requestService.updateRequest(jwt, id, accepted, convenientDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
