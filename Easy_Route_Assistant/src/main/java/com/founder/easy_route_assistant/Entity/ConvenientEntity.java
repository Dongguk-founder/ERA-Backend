package com.founder.easy_route_assistant.Entity;

import com.founder.easy_route_assistant.DTO.ConvenientDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.geo.Point;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
@Table(name="convenient")
public class ConvenientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String convenientType; // elevator, charger, bathroom

    /*@Column
    private String roadAddr;*/

    @Column
    private String description;

    @Column
    private Point point;

    // charger에만 있는 것
    @Column
    private String weekday;

    @Column
    private String holiday;

    @Column
    private String saturday;
}
