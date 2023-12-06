package com.founder.easy_route_assistant.DTO.Route;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class RouteDTO {
<<<<<<< HEAD
    private Long id;
=======
>>>>>>> 1e818e5 ([Feat] parsing json, get simple route)
    private Long totalTime;
    private List<RouteElementDTO> routeElements;
}
