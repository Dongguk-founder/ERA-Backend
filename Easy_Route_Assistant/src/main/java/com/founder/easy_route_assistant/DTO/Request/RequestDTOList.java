package com.founder.easy_route_assistant.DTO.Request;

import lombok.*;

import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTOList {
    List<RequestDTO> requestDTOList;
}
