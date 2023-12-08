package com.founder.easy_route_assistant.Entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "bus_station")
public class BusStationEntity {
    @Id
    @Column(name = "bus_route_id")
    Long busRouteId;
    @Column(name = "bus_name")
    String busName;
    @Column
    Long ord;
    @Column(name = "st_id")
    Long stId;
    @Column(name = "st_name")
    String stName;

}
