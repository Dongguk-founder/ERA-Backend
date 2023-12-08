package com.founder.easy_route_assistant.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="busstation")
public class BusStationEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Double id;
    Double busrouteid;
    String busname;
    Double ord;
    Double stid;

}
