package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.Service.BusStationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/bus")
public class BusStationController {
    private final BusStationService busStationService;
    private final Logger logger = LoggerFactory.getLogger(BusStationController.class);

    //    @GetMapping(value = "/busName")
//    public ResponseEntity<Void> getBusRouteId(@RequestParam(value = "busName") String busName) {
//        Long busRouteId  = busStationService.getBusRouteId(busName);
//        System.out.println(busRouteId);
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/busName")
    public ResponseEntity<Void> getBusRouteId(@RequestParam(value = "busName") String busName) {
        Optional<Long> busRouteId = busStationService.getBusRouteId(busName);
        busRouteId.ifPresent(id -> logger.info("Bus Route ID: {}", id));
        return ResponseEntity.ok().build();
    }

}
