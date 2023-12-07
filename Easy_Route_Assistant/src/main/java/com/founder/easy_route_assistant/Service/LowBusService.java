package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.Entity.LowBusEntity;
import com.founder.easy_route_assistant.Repository.LowBusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LowBusService {
    private final LowBusRepository lowBusRepository;
    public String filteringRoute (String busNum){
        Optional<LowBusEntity> busEntity = lowBusRepository.findById(busNum);
        if (busEntity.isPresent()){
            return busEntity.get().getRouteid();
        } else {
            return null;
        }
    }

}
