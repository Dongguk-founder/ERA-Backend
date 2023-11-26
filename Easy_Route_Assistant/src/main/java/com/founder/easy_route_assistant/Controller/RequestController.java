package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.RequestDTO;
import com.founder.easy_route_assistant.Entity.RequestEntity;
import com.founder.easy_route_assistant.Service.RequestService;
import com.founder.easy_route_assistant.token.JwtProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

        return ResponseEntity.status(HttpStatus.CREATED).body(requestDTO);
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

    @PatchMapping("/update")
    public ResponseEntity<RequestDTO> requestAccept(@RequestHeader String jwt, @RequestBody RequestDTO requestDTO) {
        HttpStatus status = requestService.updateRequest(jwt, requestDTO);

        return ResponseEntity.status(status).body(requestDTO);
    }
}
