package com.founder.easy_route_assistant.Entity;

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
@Table(name = "favorite")
public class FavoriteEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String placeName;

    @Column
    private String roadNameAddress;

    @Column
    private Point point ;

    //다대일 매핑
    @ManyToOne
    // 지정하지 않았을 때의 기본값 : 참조하는 Entity의 field명_+ "_" + 참조된 Entity의 기본 키 열의 이름 따라서 여기서는 favorite_id가 될것임
    @JoinColumn (name = "user_id")
    private UserEntity user;

}
