package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.ConvenientDTO;
import com.founder.easy_route_assistant.Service.ConvenientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ConvenientController {
    private final ConvenientService convenientService;

    @GetMapping("/add-convenient")
    public String addConvenientForm() { return "add-convenient"; }

    @PostMapping("/add-convenient")
    public String addConvenientForm(@ModelAttribute ConvenientDTO convenientDTO) {
        String addConvenient = convenientService.save(convenientDTO);

        return "index"; // url mapping 말고 response 보내기
    }

    // @RequestMapping(method = RequestMethod.GET, path="/get-convenient-list")
    @GetMapping("/get-convenient-list")
    public List<ConvenientDTO> getConvenientList() {
        /*List<ConvenientDTO> ConvenientDTOS = convenientService.getConvenientList();
        System.out.println(ConvenientDTOS);
        return ConvenientDTOS;*/

        return convenientService.getConvenientList();
    }
}
