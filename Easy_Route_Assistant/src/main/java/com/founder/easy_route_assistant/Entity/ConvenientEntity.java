package com.founder.easy_route_assistant.Entity;


import com.founder.easy_route_assistant.DTO.ConvenientDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

@Entity
@Getter
@NoArgsConstructor
@Table(name="convenient")
public class ConvenientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String convenientName;

    @Column
    private Point point;

    /*@Column
    private Double longitude;

    @Column
    private Double latitude;*/

    @Builder
    public static ConvenientEntity toConvenientEntity(ConvenientDTO convenientDTO) {
        ConvenientEntity convenientEntity = new ConvenientEntity();

        convenientEntity.convenientName = convenientDTO.getConvenientName();
        /*convenientEntity.latitude = convenientDTO.getLatitude();
        convenientEntity.longitude = convenientDTO.getLongitude();*/
        convenientEntity.point = convenientDTO.getPoint();

        return convenientEntity;
    }
}
