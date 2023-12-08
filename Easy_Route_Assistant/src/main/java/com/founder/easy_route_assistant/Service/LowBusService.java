package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.Entity.LowBusEntity;
import com.founder.easy_route_assistant.Repository.LowBusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LowBusService {
    private final LowBusRepository lowBusRepository;
    @Value("${REALTIMEBUS_URL}")
    private String REALTIMEBUS_URL;


    @Value("${REALTIMEBUS_KEY}")
    private String REALTIMEBUS_KEY;


    public Void getRealtimeBusData(){
        StringBuilder sb = new StringBuilder();
        sb.append(REALTIMEBUS_URL);
        sb.append("?ServiceKey=");
        sb.append(REALTIMEBUS_KEY);
        sb.append("&stId=");

        sb.append("&busRouteId=");



    }
    public String getRouteId (String busNum){
        Optional<LowBusEntity> busEntity = lowBusRepository.findById(busNum);
        if (busEntity.isPresent()){
            return busEntity.get().getRouteid();
        } else {
            return null;
        }
    }

}
