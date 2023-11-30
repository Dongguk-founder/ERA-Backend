package com.founder.easy_route_assistant.DTO;

import lombok.*;
import org.springframework.data.geo.Point;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
@ToString
public class ConvenientDTO {
    private String convenientType;
    // private String roadAddr;
    private String description;
    private Point point;

    private String weekday;
    private String holiday;
    private String saturday;
}
