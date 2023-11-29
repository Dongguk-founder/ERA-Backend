package com.founder.easy_route_assistant.DTO;

import lombok.*;
import org.springframework.data.geo.Point;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteRequestDTO {
    Point start;
    Point end;
}
