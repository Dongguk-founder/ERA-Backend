package com.founder.easy_route_assistant.DTO.Route;

import lombok.*;
import org.springframework.data.geo.Point;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class RouteRequestDTO {
    private Point start;
    private Point end;
}
