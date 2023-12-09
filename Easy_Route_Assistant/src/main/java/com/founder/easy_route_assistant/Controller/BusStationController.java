package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.Entity.BusStationEntity;
import com.founder.easy_route_assistant.Service.BusStationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
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
    @GetMapping("/getBusRoute")
    public ResponseEntity<Void> getBusRoute() throws IOException {
        busStationService.getRealtimeBusData(112000001,100100118,22);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getBusRoutId")
    public ResponseEntity<Void> getBusRouteId(@RequestParam(value = "busName") String busName) {
        Optional<Integer> busRouteId = busStationService.getBusRouteId(busName);
        busRouteId.ifPresent(id -> logger.info("Bus Route ID: {}", id));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getStationId")
    public ResponseEntity<Void> getStationId(@RequestParam(value = "stName") String stName) {
        System.out.println(stName);
        List<BusStationEntity> busRoute = busStationService.getStationId(stName);
        for (BusStationEntity b : busRoute){
            System.out.println(b.getBusRouteId());
        }
        return ResponseEntity.ok().build();
    }
//    @GetMapping("/getAllParam")
//    public ResponseEntity<Void> getAllparam(@RequestParam(value = "busName") String busName, @RequestParam(value = "stName") String stName) {
//        Optional<BusStationEntity> temp = busStationService.getAllParam(busName,stName);
//        System.out.println(temp.get().getBusRouteId()+"\n"+temp.get().getOrd()+"\n"+temp.get().getStId());
//        return ResponseEntity.ok().build();
//    }



}
