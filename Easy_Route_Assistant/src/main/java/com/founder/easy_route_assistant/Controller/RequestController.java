package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.RequestDTO;
import com.founder.easy_route_assistant.Entity.RequestEntity;
import com.founder.easy_route_assistant.Service.RequestService;
import com.founder.easy_route_assistant.token.JwtProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

    /*@PostMapping("/send-request")
    public String sendRequest(@ModelAttribute RequestDTO requestDTO) {
        requestService.createRequest(requestDTO);

        return "index";
    }*/

    @PostMapping("/send-request")
    public ResponseEntity<RequestDTO> createRequest(@RequestBody RequestDTO requestDTO) {
        requestService.createRequest(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(requestDTO);
    }

    @GetMapping("/my-requests") // http://localhost:8080/my-requests?jwt=
    public ResponseEntity<List<RequestDTO>> requestDTOS(@RequestParam String jwt, Model model) {
        List<RequestDTO> requestDTOs = requestService.getMyRequests(jwt);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(requestDTOs);
    }

    @GetMapping("/all-requests")
    public ResponseEntity<List<RequestDTO>> requestDTOS(Model model) {
        List<RequestDTO> requestDTOs = requestService.getAllRequests();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(requestDTOs);
    }
}
