package com.founder.easy_route_assistant.DTO.Route;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class RouteDTOList {
    /*private String start;
    private String end;*/
    private List<RouteDTO> routeDTOS;
}
