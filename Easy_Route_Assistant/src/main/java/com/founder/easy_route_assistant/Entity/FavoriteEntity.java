package com.founder.easy_route_assistant.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor @Builder
@Table(name = "favorite")
public class FavoriteEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    @Column
    private String userID;


    //일대다 매핑
    @OneToMany(mappedBy = "favorite")
    private List<FavoriteItemEntity> favoriteCollection;

}
