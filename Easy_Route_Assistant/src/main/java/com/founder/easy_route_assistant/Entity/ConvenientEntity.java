package com.founder.easy_route_assistant.Entity;

import com.founder.easy_route_assistant.DTO.ConvenientDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor @Builder
@Table(name="convenient")
public class ConvenientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String convenientName;

    @Column
    private String content;

    @Column
    private Point point; // 얘도 유일한 멤버

    @Builder
    public static ConvenientEntity toConvenientEntity(ConvenientDTO convenientDTO) {
        ConvenientEntity convenientEntity = new ConvenientEntity();

        convenientEntity.convenientName = convenientDTO.getConvenientName();
        convenientEntity.content = convenientDTO.getContent();
        convenientEntity.point = convenientDTO.getPoint();

        return convenientEntity;
    }
}
