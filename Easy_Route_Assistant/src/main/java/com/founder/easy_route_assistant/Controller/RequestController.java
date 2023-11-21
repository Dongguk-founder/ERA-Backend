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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RequestController {
    @Autowired
    private final RequestService requestService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/send-request")
    public ResponseEntity<RequestDTO> createRequest(@RequestHeader String jwt, @RequestBody RequestDTO requestDTO) {
        String userID = jwtProvider.getUserID(jwt);
        requestService.createRequest(userID, requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(requestDTO);
    }

    @GetMapping("/request-list")
    public ResponseEntity<List<RequestDTO>> requestDTOS(@RequestHeader String jwt) {
        List<RequestDTO> requestDTOS = requestService.getAllRequests(jwt);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(requestDTOS);
    }

    @PatchMapping("/request-accept")
    public ResponseEntity<RequestDTO> requestAccept(@RequestHeader String jwt, @RequestBody RequestDTO requestDTO) {
        requestService.updateRequest(jwt, requestDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(requestDTO);
    }
}
