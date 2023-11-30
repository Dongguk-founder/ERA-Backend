package com.founder.easy_route_assistant.DTO;

import lombok.*;
import org.springframework.data.geo.Point;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
@ToString
public class ChargerDTO {
    private Point point;
    private String placeDescript;
    private String weekStart;
    private String weekEnd;
    private String satStart;
    private String satEnd;
    private String holiStart;
    private String holiEnd;
}
