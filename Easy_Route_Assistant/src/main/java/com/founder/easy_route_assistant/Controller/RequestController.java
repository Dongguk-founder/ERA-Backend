package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.RequestDTO;
import com.founder.easy_route_assistant.Entity.RequestEntity;
import com.founder.easy_route_assistant.Service.RequestService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/send-request")
    public String sendRequest(@ModelAttribute RequestDTO requestDTO) {
        requestService.createRequest(requestDTO);

        return "index";
    }

    /*@GetMapping("/my-request-{userID}")
    public List<RequestDTO> requestDTOS() {

    }*/
}
