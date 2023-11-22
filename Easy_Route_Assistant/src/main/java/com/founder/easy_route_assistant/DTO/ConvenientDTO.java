package com.founder.easy_route_assistant.DTO;

import com.founder.easy_route_assistant.Entity.ConvenientEntity;
import com.founder.easy_route_assistant.Entity.RequestEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.geo.Point;

@Getter @Setter
@NoArgsConstructor
@ToString
public class ConvenientDTO {
    private String convenientName;
    private String content;
    private Point point;

    public static ConvenientDTO toConvenientDTO(ConvenientEntity convenientEntity) {
        ConvenientDTO convenientDTO = new ConvenientDTO();

        convenientDTO.setConvenientName(convenientEntity.getConvenientName());
        convenientDTO.setContent(convenientEntity.getContent());
        convenientDTO.setPoint(convenientEntity.getPoint());

        return convenientDTO;
    }

    public static ConvenientDTO toConvenient(RequestEntity requestEntity) {
        ConvenientDTO convenientDTO = new ConvenientDTO();

        convenientDTO.setConvenientName(requestEntity.getConvenientName());
        convenientDTO.setContent(requestEntity.getContent());
        convenientDTO.setPoint(requestEntity.getPoint());

        return convenientDTO;
    }
}
