package com.founder.easy_route_assistant.DTO.Route;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class DetailRouteDTO {
    private String totalTime;
    private List<DetailElementDTO> detailElements;
}
