package com.founder.easy_route_assistant.DTO;

import lombok.*;
import org.springframework.data.geo.Point;

@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor @Builder
public class RequestDTO {
    private int id; // request끼리 구분하기 위한 용도

    private String convenientType; // elevator, charger, bathroom
    private Point point;
    private String roadAddr;
    private String content;
    private boolean accepted;

    private String userID;
}
