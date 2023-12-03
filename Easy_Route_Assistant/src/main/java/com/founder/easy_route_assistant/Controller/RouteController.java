package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.Route.RouteDTOList;
import com.founder.easy_route_assistant.DTO.Route.RouteRequestDTO;
import com.founder.easy_route_assistant.Service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/map")
public class RouteController {
    private final RouteService routeService;

    @PostMapping("/find")
    public ResponseEntity<RouteDTOList> findRoute(@RequestBody RouteRequestDTO routeRequestDTO) throws IOException {
        RouteDTOList routeDTOList = routeService.searchRoute(routeRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(routeDTOList);
    }
}
