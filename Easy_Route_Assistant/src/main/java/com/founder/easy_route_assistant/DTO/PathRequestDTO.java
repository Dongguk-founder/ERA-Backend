package com.founder.easy_route_assistant.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.awt.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PathRequestDTO {
    Point start;
    Point end;
}
