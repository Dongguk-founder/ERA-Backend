package com.founder.easy_route_assistant.DTO.Convenient;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConvenientListDTO {
    List<ConvenientDTO> convenientDTOList;
}
