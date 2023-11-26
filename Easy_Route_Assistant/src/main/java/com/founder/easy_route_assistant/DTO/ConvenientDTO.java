package com.founder.easy_route_assistant.DTO;

import lombok.*;
import org.springframework.data.geo.Point;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
@ToString
public class ConvenientDTO {
    private String convenientName;
    private String content;
    private Point point;
}
