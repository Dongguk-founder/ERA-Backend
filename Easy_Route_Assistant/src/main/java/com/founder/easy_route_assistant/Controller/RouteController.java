package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.Route.*;
import com.founder.easy_route_assistant.Repository.RouteRepository;
import com.founder.easy_route_assistant.Service.RouteService;
import lombok.RequiredArgsConstructor;
import okhttp3.Route;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/get/{id}")
    public ResponseEntity<DetailRouteDTO> getRoute(@PathVariable Long id) {
        DetailRouteDTO detailRoute = routeService.mapRoute(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(detailRoute);
    }
}
