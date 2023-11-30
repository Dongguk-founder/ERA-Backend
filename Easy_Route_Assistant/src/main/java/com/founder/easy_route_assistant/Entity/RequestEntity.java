package com.founder.easy_route_assistant.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.geo.Point;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
@Table(name="request")
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String convenientType;

    @Column
    private Point point;

    @Column
    private String roadAddr;

    @Column
    private String content;

    @Column
    private Boolean accepted = false;

    @ManyToOne
    @JoinColumn(name="userid")
    private UserEntity userEntity;
}
