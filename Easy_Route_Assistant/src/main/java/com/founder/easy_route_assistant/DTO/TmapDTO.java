package com.founder.easy_route_assistant.DTO;

import lombok.*;
import org.springframework.data.geo.Point;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class TmapDTO {
    private Point start;
    private Point end;
}
