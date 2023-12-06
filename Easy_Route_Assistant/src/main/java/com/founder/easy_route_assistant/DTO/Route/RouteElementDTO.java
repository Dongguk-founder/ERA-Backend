package com.founder.easy_route_assistant.DTO.Route;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class RouteElementDTO {
    private String start;
    private String end;
    private String mode; // walk, subway, bus
    private String routeColor;
    private String name; // bus: 버스 번호, subway: 지하철 호선
}
