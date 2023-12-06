package com.founder.easy_route_assistant.DTO.Convenient;

import lombok.*;
import org.springframework.data.geo.Point;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
@ToString
public class BathroomDTO {
    private Point point;
    private String description; // 역 이름, 상세 위치
    private String weekday;
}
