package com.founder.easy_route_assistant.DTO.Route;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class RouteElementDTO {
    private String start;
    private String end;
    private String mode; // subway, bus
    private String routeColor;
    private String name; // bus: 버스 번호, subway: 지하철 방향
    private String line; // bus: null, subway: n호선의 n
    private Long distance;
    private Long sectionTime;
    private String arrmsg1;
    private String arrmsg2;
}
