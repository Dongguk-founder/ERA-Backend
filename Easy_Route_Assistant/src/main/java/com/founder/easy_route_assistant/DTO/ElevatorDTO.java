package com.founder.easy_route_assistant.DTO;


import lombok.*;
import org.springframework.data.geo.Point;


@Getter @Setter
public class ElevatorDTO {
    private String subway_name;
    private Point point;
}
