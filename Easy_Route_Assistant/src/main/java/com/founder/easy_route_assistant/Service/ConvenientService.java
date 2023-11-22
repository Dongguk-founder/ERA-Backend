package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.ConvenientDTO;
import com.founder.easy_route_assistant.Entity.ConvenientEntity;
import com.founder.easy_route_assistant.Repository.ConvenientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConvenientService {
    @Autowired
    ConvenientRepository convenientRepository;

    public String save(ConvenientDTO convenientDTO) {
        ConvenientEntity convenientEntity = ConvenientEntity.toConvenientEntity(convenientDTO);

        convenientRepository.save(convenientEntity);

        return "편의 시설 등록 성공";
    }

    public List<ConvenientDTO> getConvenientList() {
        List<ConvenientEntity> convenientEntities = convenientRepository.findAll();
        List<ConvenientDTO> convenientDTOS = new ArrayList<>();

        for(ConvenientEntity convenientEntity : convenientEntities) {
            ConvenientDTO convenientDTO = ConvenientDTO.toConvenientDTO(convenientEntity);

            convenientDTOS.add(convenientDTO);
        }

        return convenientDTOS;
    }
}
