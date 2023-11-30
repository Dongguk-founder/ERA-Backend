package com.founder.easy_route_assistant.DTO.Request;

import lombok.*;
import org.springframework.data.geo.Point;

@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor @Builder
public class RequestDTO {
    private int id; // request끼리 구분하기 위해 Entity에서 id 받아오기 but,

    private String convenientType; // elevator, charger, bathroom
    private Point point;
    private String roadAddr;
    private String description;

    private String weekday;
    private String holiday;
    private String saturday;
    private boolean accepted;

    private String userID;
}
