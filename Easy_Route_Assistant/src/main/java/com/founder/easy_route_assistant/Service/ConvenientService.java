package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.ConvenientDTO;
import com.founder.easy_route_assistant.Entity.ConvenientEntity;
import com.founder.easy_route_assistant.Repository.ConvenientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConvenientService {

    private final ConvenientRepository convenientRepository;

    public ConvenientDTO save(ConvenientDTO convenientDTO) {
        ConvenientEntity convenientEntity = ConvenientEntity.builder()
                .convenientName(convenientDTO.getConvenientName())
                .content(convenientDTO.getContent())
                .point(convenientDTO.getPoint())
                .build();
        System.out.println(convenientDTO);
        convenientRepository.save(convenientEntity);

        return convenientDTO;
    }

    public List<ConvenientDTO> getConvenientList() {
        List<ConvenientEntity> convenientEntities = convenientRepository.findAll();
        List<ConvenientDTO> convenientDTOS = new ArrayList<>();

        for (ConvenientEntity convenientEntity : convenientEntities) {
            ConvenientDTO convenientDTO = ConvenientDTO.builder()
                    .convenientName(convenientEntity.getConvenientName())
                    .content(convenientEntity.getContent())
                    .point(convenientEntity.getPoint())
                    .build();
            convenientDTOS.add(convenientDTO);
        }

        return convenientDTOS;
    }
}
