package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.Convenient.ConvenientDTO;
import com.founder.easy_route_assistant.DTO.Convenient.ConvenientListDTO;
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
    private final ElevatorService elevatorService;
    private final ChargerService chargerService;

    public void update(ConvenientDTO convenientDTO) {
        ConvenientEntity convenientEntity = convenientRepository.findByPoint(convenientDTO.getPoint());

        if (convenientEntity != null) { // 기존 항목 수정일 때
            if (convenientDTO.getConvenientType() != null) convenientEntity.setConvenientType(convenientDTO.getConvenientType());
            if (convenientDTO.getDescription() != null) convenientEntity.setDescription(convenientDTO.getDescription());
            if (convenientDTO.getWeekday() != null) convenientEntity.setWeekday(convenientDTO.getWeekday());
            if (convenientDTO.getSaturday() != null) convenientEntity.setSaturday(convenientDTO.getSaturday());
            if (convenientDTO.getHoliday() != null) convenientEntity.setHoliday(convenientDTO.getHoliday());

            /*// 주소 정보는 바꿀 필요 없을 것 같은데 어떻게 생각?
            if (convenientDTO.getRoadAddr() != null) convenientEntity.setRoadAddr(convenientDTO.getRoadAddr());
            if (convenientDTO.getPoint() != null) convenientEntity.setPoint(convenientDTO.getPoint());*/

            convenientRepository.save(convenientEntity);
        }
        else { // 새로 등록일 때
            ConvenientEntity newConvenient = ConvenientEntity.builder()
                    .convenientType(convenientDTO.getConvenientType())
                    .point(convenientDTO.getPoint())
                    // .roadAddr(convenientDTO.getRoadAddr())
                    .description(convenientDTO.getDescription())
                    .weekday(convenientDTO.getWeekday())
                    .saturday(convenientDTO.getSaturday())
                    .holiday(convenientDTO.getHoliday())
                    .build();

            convenientRepository.save(newConvenient);
        }

        // convenientRepository.save(convenientEntity);
    }

    public ConvenientListDTO getConvenientList(String convenientType) {
        // List<ConvenientEntity> convenientEntities = convenientRepository.findAll();
        List<ConvenientEntity> convenientEntities = convenientRepository.findAllByConvenientType(convenientType);
        List<ConvenientDTO> convenientDTOS = new ArrayList<>();

        ConvenientListDTO convenientListDTO = new ConvenientListDTO();

        for (ConvenientEntity convenientEntity : convenientEntities) {
            ConvenientDTO convenientDTO = ConvenientDTO.builder()
                    .convenientType(convenientEntity.getConvenientType())
                    // .roadAddr(convenientEntity.getRoadAddr())
                    .description(convenientEntity.getDescription())
                    .point(convenientEntity.getPoint())
                    .weekday(convenientEntity.getWeekday())
                    .saturday(convenientEntity.getSaturday())
                    .holiday(convenientEntity.getHoliday())
                    .build();
            convenientDTOS.add(convenientDTO);
        }

        if (convenientType.equals("elevator")) { // elevator api
            List<ConvenientDTO> elevatorDTOS = elevatorService.requestElevatorAPI("중구");
            System.out.println("api: " + elevatorDTOS);
            convenientDTOS.addAll(elevatorDTOS);
        }
        else if (convenientType.equals("charger")) {
            List<ConvenientDTO> chargerDTOS = chargerService.requestChargerAPI("중구");
            convenientDTOS.addAll(chargerDTOS);
        }
        else {
            // bathroom api
        }

        convenientListDTO.setConvenientDTOList(convenientDTOS);

        return convenientListDTO;
    }
}
