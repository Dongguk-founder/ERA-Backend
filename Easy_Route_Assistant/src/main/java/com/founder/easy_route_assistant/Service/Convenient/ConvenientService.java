package com.founder.easy_route_assistant.Service.Convenient;

import com.founder.easy_route_assistant.DTO.Convenient.BathroomDTO;
import com.founder.easy_route_assistant.DTO.Convenient.ConvenientDTO;
import com.founder.easy_route_assistant.DTO.Convenient.ConvenientListDTO;
import com.founder.easy_route_assistant.Entity.ConvenientEntity;
import com.founder.easy_route_assistant.Repository.ConvenientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Primary
@Service
@RequiredArgsConstructor
public class ConvenientService {

    private final ConvenientRepository convenientRepository;
    private final ElevatorService elevatorService;
    private final ChargerService chargerService;
    private final BathroomService bathroomService;

    public void update(ConvenientDTO convenientDTO) {
        ConvenientEntity convenientEntity = convenientRepository.findByPoint(convenientDTO.getPoint());

        if (convenientEntity != null) { // 기존 항목 수정일 때
            if (convenientDTO.getConvenientType() != null)
                convenientEntity.setConvenientType(convenientDTO.getConvenientType());
            if (convenientDTO.getDescription() != null) convenientEntity.setDescription(convenientDTO.getDescription());
            if (convenientDTO.getWeekday() != null) convenientEntity.setWeekday(convenientDTO.getWeekday());
            if (convenientDTO.getSaturday() != null) convenientEntity.setSaturday(convenientDTO.getSaturday());
            if (convenientDTO.getHoliday() != null) convenientEntity.setHoliday(convenientDTO.getHoliday());

            /*// 주소 정보는 바꿀 필요 없을 것 같은데 어떻게 생각?
            if (convenientDTO.getRoadAddr() != null) convenientEntity.setRoadAddr(convenientDTO.getRoadAddr());
            if (convenientDTO.getPoint() != null) convenientEntity.setPoint(convenientDTO.getPoint());*/

            convenientRepository.save(convenientEntity);
        } else { // 새로 등록일 때
            ConvenientEntity newConvenient = ConvenientEntity.builder()
                    .convenientType(convenientDTO.getConvenientType())
                    .point(convenientDTO.getPoint())
                    .description(convenientDTO.getDescription())
                    .weekday(convenientDTO.getWeekday())
                    .saturday(convenientDTO.getSaturday())
                    .holiday(convenientDTO.getHoliday())
                    .build();

            convenientRepository.save(newConvenient);
        }
    }

    public ConvenientListDTO getConvenientList(String convenientType) {
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
            convenientDTOS.addAll(elevatorDTOS);
        } else if (convenientType.equals("charger")) {
            List<ConvenientDTO> chargerDTOS = chargerService.requestChargerAPI("중구");
            convenientDTOS.addAll(chargerDTOS);
        } else if ( convenientType.equals("bathroom")) {
            List<ConvenientDTO> bathroomDTOS = bathroomService.requestBathroomAPI();
            convenientDTOS.addAll(bathroomDTOS);
        } else {
            System.out.println("잘못된 편의시설 타입을 입력하였습니다.");
        }

        convenientListDTO.setConvenientDTOList(convenientDTOS);

        return convenientListDTO;
}
}
