package com.founder.easy_route_assistant.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class RealTimeParamDTO {
    Integer busRouteId;
    Integer ord;
    Integer stId;
}
