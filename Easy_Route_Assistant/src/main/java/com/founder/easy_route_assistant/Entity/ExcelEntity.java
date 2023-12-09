package com.founder.easy_route_assistant.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "subwaycode")
@NoArgsConstructor
@AllArgsConstructor @Builder
public class ExcelEntity {
    @Id
    private Long id;

    @Column(name = "opr_code")
    private String opr_code;
    @Column(name = "station_code")
    private String stationCode;
    @Column(name = "station_name")
    private String stationName;
    @Column(name = "line_num")
    private String lineNum;
    @Column (name = "line_code")
    private String lineCode;
}
