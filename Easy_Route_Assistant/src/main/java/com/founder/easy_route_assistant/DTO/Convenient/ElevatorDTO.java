package com.founder.easy_route_assistant.DTO.Convenient;

import lombok.*;
import org.springframework.data.geo.Point;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
@ToString
public class ElevatorDTO {
    private String sw_cd;
    private Point point;
}
