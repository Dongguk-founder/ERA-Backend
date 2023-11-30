package com.founder.easy_route_assistant.DTO;

import com.founder.easy_route_assistant.Entity.RequestEntity;
import com.founder.easy_route_assistant.Entity.UserEntity;
import lombok.*;
import org.springframework.data.geo.Point;

@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor @Builder
public class RequestDTO {
    private int id; // request끼리 구분하기 위해 Entity에서 id 받아오기 but,
    private String convenientName;
    private Point point;
    private String content;
    private boolean accepted;
    private String userID;
}
