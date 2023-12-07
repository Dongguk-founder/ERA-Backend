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

    @Column
    private String opr_code;
    @Column
    private String stationCode;
    @Column
    private String stationName;
    @Column
    private String lineNum;
    @Column
    private String lineCode;
}
